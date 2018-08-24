package com.syswin.temail.channel.connection;

import com.syswin.temail.channel.connection.handler.HeartBeatServerHandler;
import com.syswin.temail.channel.connection.handler.ServerStatusHandler;
import com.syswin.temail.channel.connection.handler.UserStatusHandler;
import com.syswin.temail.channel.core.codec.StatusRequestDecoder;
import com.syswin.temail.channel.core.codec.StatusResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.net.InetSocketAddress;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Slf4j
//@Component
public class CdtpStatusServer implements ApplicationRunner {

  @Autowired
  private CdtpStatusProperties properties;

  public Channel getChannel() {
    return null;
  }

  @Override
  public void run(ApplicationArguments args) {

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(properties.getServerPort()))
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline()
                  .addLast(new IdleStateHandler(600, 0, 0))
                  .addLast(new HeartBeatServerHandler())
                  .addLast("lengthFieldDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4))
                  .addLast("lengthFieldEncoder", new LengthFieldPrepender(4))
                  .addLast("statusRequestDecoder", new StatusRequestDecoder())
                  .addLast("statusResponseEncoder", new StatusResponseEncoder())
                  .addLast("serverStatusHandler", new ServerStatusHandler())
                  .addLast("userStatusHandler", new UserStatusHandler());
            }
          });

      ChannelFuture future = bootstrap.bind().sync();
      future.channel().closeFuture().sync();
    } catch (Exception e) {
      //log.error("状态服务器异常中止！", e);
      System.exit(-1);
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
