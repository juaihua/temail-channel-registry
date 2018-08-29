package com.syswin.temail.channel.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.syswin.temail.channel.account.beans.CdtpServer;
import io.netty.channel.Channel;

public class ChannelHolder {
  public static Map<Channel, CdtpServer> channelMap = new ConcurrentHashMap<>();
}
