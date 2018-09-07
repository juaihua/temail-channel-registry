package com.syswin.temail.channel.client.connection;

import com.syswin.temail.channel.core.entity.BizType;
import com.syswin.temail.channel.core.entity.HeartBeatRequest;
import com.syswin.temail.channel.core.entity.StatusRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-8-20
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatClientHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {

  private static final StatusRequest<HeartBeatRequest> HEARTBEAT
      = new StatusRequest<>(BizType.HEART_BEAT, new HeartBeatRequest());

  @Override
  protected void eventReceived(ChannelHandlerContext ctx, IdleStateEvent evt) {
    IdleState state = evt.state();
    if (state == IdleState.WRITER_IDLE) {
      log.debug("发送心跳信息！");
      ctx.channel().writeAndFlush(HEARTBEAT);
    }
  }

}
