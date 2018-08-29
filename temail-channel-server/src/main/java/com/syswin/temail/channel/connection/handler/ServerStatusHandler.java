package com.syswin.temail.channel.connection.handler;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import com.syswin.temail.channel.core.entity.ServerStatusRequest;
import com.syswin.temail.channel.core.entity.StatusRequest;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import static com.syswin.temail.channel.connection.ChannelHolder.channelMap;

@Slf4j
@Sharable
public class ServerStatusHandler extends SimpleChannelInboundHandler<StatusRequest<?>> {

  private TemailAcctStsService statusService;

  public ServerStatusHandler(TemailAcctStsService statusService) {
    this.statusService = statusService;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, StatusRequest<?> msg) {
    switch (msg.getBizType()) {
      case HEART_BEAT:
        break;
      case SERVER_STATUS:
        ServerStatusRequest request = (ServerStatusRequest) msg.getRequestBody();
        CdtpServer cdtpServer = new CdtpServer();
        cdtpServer.setIp(request.getIp());
        cdtpServer.setProcessId(request.getProcessId());
        statusService.registerOrRecorveryServer(cdtpServer);
        channelMap.put(ctx.channel(), cdtpServer);
        log.info("客户端注册成功，IP={},ProcessId={}", request.getIp(), request.getProcessId());
        break;
      case USER_STATUS:
      default:
        throw new UnsupportedOperationException("暂不支持的业务类型：" + msg.getBizType());
    }

  }
}
