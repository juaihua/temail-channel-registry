package com.syswin.temail.cdtp.status.core.codec;

import com.google.gson.Gson;
import com.syswin.temail.cdtp.status.core.entity.BizType;
import com.syswin.temail.cdtp.status.core.entity.StatusResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Sharable
public class StatusResponseDecoder extends MessageToMessageDecoder<ByteBuf> {

  private Gson gson = new Gson();

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
    byte b = msg.readByte();
    BizType bizType = BizType.valueOf(b);
    Class<?> responseClass = bizType.getResponseClass();
    out.add(new StatusResponse<>(bizType,
        gson.fromJson(msg.toString(StandardCharsets.UTF_8), responseClass)));
  }
}
