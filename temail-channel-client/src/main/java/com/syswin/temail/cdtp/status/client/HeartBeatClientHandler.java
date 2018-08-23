package com.syswin.temail.cdtp.status.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-8-20
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatClientHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {

  private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
      CharsetUtil.UTF_8));

  @Override
  protected void eventReceived(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
    IdleState state = evt.state();
    if (state == IdleState.WRITER_IDLE) {
      // write heartbeat to server
      ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
    }
  }

}
