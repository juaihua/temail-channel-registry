package com.syswin.temail.channel.connection.handler;

import com.syswin.temail.channel.core.entity.TemailAcctStsUpdReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserStatusHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof TemailAcctStsUpdReq) {
      throw new UnsupportedOperationException("暂还不支持用户信息通讯！");
    }
  }

}
