package com.syswin.temail.channel.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-8-22
 */
@Data
@ConfigurationProperties(prefix = "channel.client")
public class TemailChannelClientProperties {

  private String host = "127.0.0.1";
  private int port = 8765;
  private int writeIdle = 10;
  private int maxRetryInternal = 60;

}
