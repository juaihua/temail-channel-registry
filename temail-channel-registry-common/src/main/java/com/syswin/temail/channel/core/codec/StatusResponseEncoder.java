package com.syswin.temail.channel.core.codec;

import com.google.gson.Gson;
import com.syswin.temail.channel.core.entity.StatusResponse;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
public class StatusResponseEncoder extends MessageToMessageEncoder<StatusResponse<?>> {

  private Gson gson = new Gson();

  @Override
  protected void encode(ChannelHandlerContext ctx, StatusResponse<?> msg, List<Object> out) {
    out.add((byte) msg.getBizType().getCode());
    String jsonData = gson.toJson(msg.getResponseBody());
    out.add(ByteBufUtil
        .encodeString(ctx.alloc(), CharBuffer.wrap(jsonData), StandardCharsets.UTF_8));
  }
}
