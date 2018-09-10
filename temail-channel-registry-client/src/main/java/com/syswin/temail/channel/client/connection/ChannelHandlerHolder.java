package com.syswin.temail.channel.client.connection;

import io.netty.channel.ChannelHandler;

/**
 * @author 姚华成
 * @date 2018-08-21
 */
public interface ChannelHandlerHolder {
    ChannelHandler[] handlers();
}
