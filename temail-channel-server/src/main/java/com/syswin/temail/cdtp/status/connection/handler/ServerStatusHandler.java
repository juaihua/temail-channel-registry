package com.syswin.temail.cdtp.status.connection.handler;

import com.syswin.temail.cdtp.status.core.entity.ServerStatusRequestBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
public class ServerStatusHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof ServerStatusRequestBody) {
      System.out.println(msg);
    }
  }
}
