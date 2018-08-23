package com.syswin.temail.channel.connection;

import com.syswin.temail.channel.account.beans.CdtpServer;
import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 姚华成
 * @date 2018-8-23
 */
public class ChannelHolder {
  public static Map<Channel, CdtpServer> channelMap = new ConcurrentHashMap<>();
}
