package com.syswin.temail.channel.connection.handler;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import static com.syswin.temail.channel.connection.ChannelHolder.channelMap;

@Slf4j
public class IdleHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {

  private TemailAcctStsService connectionStatusService;

  public IdleHandler(
      TemailAcctStsService connectionStatusService) {
    this.connectionStatusService = connectionStatusService;
  }

  @Override
  protected void eventReceived(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
    log.info("连接空闲超时，通道关闭！");
    endChannel(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("连接错误", cause);
    endChannel(ctx);
  }

  private void endChannel(ChannelHandlerContext ctx) throws InterruptedException {
    CdtpServer cdtpServer = channelMap.remove(ctx.channel());
    if (cdtpServer != null) {
      connectionStatusService.offLineTheServer(cdtpServer);
    }
    ctx.close().sync();
  }

}
