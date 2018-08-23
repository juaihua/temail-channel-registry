package com.syswin.temail.channel.connection.handler;

import com.syswin.temail.channel.core.entity.TemailAccountStatusUpdateRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
public class UserStatusHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof TemailAccountStatusUpdateRequest) {
      System.out.println(msg);
      // 此处处理
    }
  }

}
