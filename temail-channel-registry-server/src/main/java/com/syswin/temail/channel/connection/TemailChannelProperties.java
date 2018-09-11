package com.syswin.temail.channel.connection;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "channel")
public class TemailChannelProperties {

  private int serverPort;
  private int readIdle;

}
