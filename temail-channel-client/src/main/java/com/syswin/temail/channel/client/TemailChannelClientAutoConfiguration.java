package com.syswin.temail.channel.client;

import com.syswin.temail.channel.client.connection.ChannelManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 姚华成
 * @date 2018-8-20
 */
@Configuration
@EnableConfigurationProperties(TemailChannelClientProperties.class)
public class TemailChannelClientAutoConfiguration {

  @Bean
  public TemailChannelClient temailChannelClient(ChannelManager channelManager) {
    return new TemailChannelClient(channelManager);
  }

  @Bean
  public ChannelManager channelManager(TemailChannelClientProperties properties) {
    return new ChannelManager(properties);
  }

}
