package com.syswin.temail.channel.loginhistory;

import com.syswin.temail.channel.TemailChannelProperties;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginHistoryConfiguration {

  @Bean
  public LoginHistoryRunner loginHistorySerivce(MQProducer mqProducer, TemailChannelProperties properties) {
    return new LoginHistoryRunner(mqProducer, properties.getRocketmq().getMqTopic());
  }

}
