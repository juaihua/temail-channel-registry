package com.syswin.temail.channel.core.codec;

import com.google.gson.Gson;
import com.syswin.temail.channel.core.entity.StatusRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Sharable
public class StatusRequestEncoder extends MessageToByteEncoder<StatusRequest<?>> {

  private Gson gson = new Gson();

  @Override
  protected void encode(ChannelHandlerContext ctx, StatusRequest<?> msg, ByteBuf out) throws Exception {
    out.writeByte(msg.getBizType().getCode());
    out.writeBytes(gson.toJson(msg.getRequestBody()).getBytes());
  }
}
