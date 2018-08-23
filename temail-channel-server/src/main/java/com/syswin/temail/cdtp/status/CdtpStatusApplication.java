package com.syswin.temail.cdtp.status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CdtpStatusProperties.class)
public class CdtpStatusApplication {

  public static void main(String[] args) {
    SpringApplication.run(CdtpStatusApplication.class, args);
  }
}
