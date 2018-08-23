package com.syswin.temail.channel.connection;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-8-23
 */
@Data
@ConfigurationProperties(prefix = "channel")
public class CdtpStatusProperties {

  private int serverPort;

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }
}
