package com.syswin.temail.channel.client.connection;

import com.syswin.temail.channel.client.TemailChannelClientProperties;
import com.syswin.temail.channel.core.codec.StatusRequestEncoder;
import com.syswin.temail.channel.core.codec.StatusResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-08-21
 */
@Slf4j
public class ChannelManager {

  private final Timer timer = new HashedWheelTimer();
  private final int lengthFieldLength = 4;
  @Setter
  @Getter
  private Channel channel;
  private TemailChannelClientProperties properties;

  public ChannelManager(TemailChannelClientProperties properties) {
    this.properties = properties;
  }

  @PostConstruct
  public void connect() throws InterruptedException {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));

    final ConnectionWatchdog watchdog = new ConnectionWatchdog(this, bootstrap, timer,
        properties) {

      public ChannelHandler[] handlers() {
        return new ChannelHandler[]{
            this,
            new IdleStateHandler(0, properties.getWriteIdle(), 0),
            new HeartBeatClientHandler(),
            new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, lengthFieldLength),
            new LengthFieldPrepender(lengthFieldLength),
            new StatusResponseDecoder(),
            new StatusRequestEncoder(),
        };
      }
    };

    //进行连接
    ChannelFuture future;
    synchronized (bootstrap) {
      bootstrap.handler(new ChannelInitializer<Channel>() {
        //初始化channel
        @Override
        protected void initChannel(Channel ch) {
          ch.pipeline().addLast(watchdog.handlers());
        }
      });

      future = bootstrap.connect(properties.getHost(), properties.getPort()).sync();
    }
    // 以下代码在synchronized同步块外面是安全的
    setChannel(future.channel());
  }

}
