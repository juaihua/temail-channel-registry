package com.syswin.temail.channel.connection.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-08-21
 */
@Slf4j
public class HeartBeatServerHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {

  @Override
  protected void eventReceived(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
    log.info("连接空闲超时，通道关闭！");
    ctx.close();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("连接错误", cause);
    ctx.close();
  }
}
