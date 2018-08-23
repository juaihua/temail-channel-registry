package com.syswin.temail.channel.connection.handler;

import com.syswin.temail.channel.core.entity.TemailAccountStatusUpdateRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Slf4j
public class UserStatusHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof TemailAccountStatusUpdateRequest) {
      throw new UnsupportedOperationException("暂还不支持用户信息通讯！");
    }
  }

}
