package com.syswin.temail.cdtp.status.client;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 姚华成
 * @date 2018-8-20
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(CdtpStatusClientProperties.class)
public class CdtpStatusClientAutoConfiguration {

  @Bean
  public CdtpStatusClient cdtpStatusClient(ChannelManager channelManager) {
    return new CdtpStatusClient(channelManager);
  }

  @Bean
  public ChannelManager channelManager(CdtpStatusClientProperties properties) {
    return new ChannelManager(properties);
  }

}
