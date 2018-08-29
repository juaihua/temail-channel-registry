package com.syswin.temail.channel.connection;

import com.syswin.temail.channel.account.service.TemailAcctStsService;
import com.syswin.temail.channel.connection.handler.IdleHandler;
import com.syswin.temail.channel.connection.handler.ServerStatusHandler;
import com.syswin.temail.channel.connection.handler.UserStatusHandler;
import com.syswin.temail.channel.core.codec.StatusRequestDecoder;
import com.syswin.temail.channel.core.codec.StatusResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemailChannelServer implements ApplicationRunner {

  public static final int LENGTH_FIELD_LENGTH = 4;
  private TemailChannelProperties properties;
  private TemailAcctStsService connectionStatusService;

  public TemailChannelServer(TemailChannelProperties properties,
      TemailAcctStsService connectionStatusService) {
    this.properties = properties;
    this.connectionStatusService = connectionStatusService;
  }

  @Override
  public void run(ApplicationArguments args) throws InterruptedException {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      int serverPort = properties.getServerPort();
      bootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline()
                  .addLast(new IdleStateHandler(properties.getReadIdle(), 0, 0))
                  .addLast(new IdleHandler(connectionStatusService))
                  .addLast("lengthFieldDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
                      LENGTH_FIELD_LENGTH, 0, LENGTH_FIELD_LENGTH))
                  .addLast("lengthFieldEncoder", new LengthFieldPrepender(LENGTH_FIELD_LENGTH))
                  .addLast("statusRequestDecoder", new StatusRequestDecoder())
                  .addLast("statusResponseEncoder", new StatusResponseEncoder())
                  .addLast("serverStatusHandler", new ServerStatusHandler(connectionStatusService))
                  .addLast("userStatusHandler", new UserStatusHandler());
            }
          });

      bootstrap.bind(serverPort).sync();
      log.info("通道状态监听服务器已经启动，端口号：{}", serverPort);
      // 由于当前进程在springboot启动时由主线执行，因此不能使用阻塞的方法
      // 同时不需要进行进行资源的优雅关闭
      // future.channel().closeFuture().sync();
    } catch (Exception e) {
      log.error("状态服务器异常中止！", e);
      throw e;
    }
  }
}
