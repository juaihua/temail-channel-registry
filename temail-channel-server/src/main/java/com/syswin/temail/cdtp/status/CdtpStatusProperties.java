package com.syswin.temail.cdtp.status;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Data
@ConfigurationProperties(prefix = "cdtp.status")
public class CdtpStatusProperties {
  private int serverPort;
}
