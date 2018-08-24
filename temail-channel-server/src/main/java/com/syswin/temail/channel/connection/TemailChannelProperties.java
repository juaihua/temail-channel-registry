package com.syswin.temail.channel.connection;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-8-23
 */
@Data
@ConfigurationProperties(prefix = "channel")
public class TemailChannelProperties {

  private int serverPort;
  private int readIdle;

}
