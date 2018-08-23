package com.syswin.temail.cdtp.status.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-8-22
 */
@Data
@ConfigurationProperties(prefix = "")
public class CdtpStatusClientProperties {
  private String host;
  private int port;
  private int maxAttempts;
  private int readIdle;
  private int writeIdle;
  private int allIdle;

}
