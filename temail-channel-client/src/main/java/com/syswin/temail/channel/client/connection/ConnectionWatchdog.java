package com.syswin.temail.channel.client.connection;

import com.syswin.temail.channel.client.TemailChannelClientProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-08-21
 */
@Slf4j
@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask,
    ChannelHandlerHolder {

  private final Bootstrap bootstrap;
  private ChannelManager channelManager;
  private Timer timer;
  private int attempts;
  private TemailChannelClientProperties properties;

  public ConnectionWatchdog(ChannelManager channelManager, Bootstrap bootstrap, Timer timer,
      TemailChannelClientProperties properties) {
    this.channelManager = channelManager;
    this.bootstrap = bootstrap;
    this.timer = timer;
    this.properties = properties;
  }

  /**
   * channel链路每次active的时候，将其连接的次数重新☞ 0
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    log.info("当前链路已经激活了，重连尝试次数重新置为0");
    attempts = 0;
    ctx.fireChannelActive();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    log.info("链接关闭，将进行重连");

    int timeout = 1 << attempts;
    if (timeout > properties.getMaxRetryInternal()) {
      timeout = properties.getMaxRetryInternal();
    } else {
      attempts++;
    }

    //重连的间隔时间会越来越长
    timer.newTimeout(this, timeout, TimeUnit.SECONDS);
    ctx.fireChannelInactive();
  }

  @Override
  public void run(Timeout timeout) throws Exception {

    ChannelFuture future;
    //bootstrap已经初始化好了，只需要将handler填入就可以了
    synchronized (bootstrap) {
      bootstrap.handler(new ChannelInitializer<Channel>() {

        @Override
        protected void initChannel(Channel ch) {
          ch.pipeline().addLast(handlers());
        }
      });
      future = bootstrap.connect(properties.getServerHost(), properties.getServerPort()).sync();
      channelManager.setChannel(future.channel());
    }

    //future对象
    future.addListener((ChannelFutureListener) f -> {
      //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
      if (!f.isSuccess()) {
        log.info("重连失败");
        f.channel().pipeline().fireChannelInactive();
      } else {
        log.info("重连成功");
      }
    });

  }
}