package com.syswin.temail.channel.core.codec;

import com.google.gson.Gson;
import com.syswin.temail.channel.core.entity.BizType;
import com.syswin.temail.channel.core.entity.StatusRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
public class StatusRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

  private Gson gson = new Gson();

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
    byte b = msg.readByte();
    BizType bizType = BizType.valueOf(b);
    Class<?> requestClass = bizType.getRequestClass();
    out.add(new StatusRequest<>(bizType,
        gson.fromJson(msg.toString(StandardCharsets.UTF_8), requestClass)));
  }
}
