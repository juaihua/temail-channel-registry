package com.syswin.temail.channel;


import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.channel")
@Data
@Component
public class TemailChannelProperties {

  private RocketMQ rocketmq;

  @ConfigurationProperties(prefix = "app.channel.rocketmq")
  @Component
  @Data
  @ToString
  public static class RocketMQ {

    private String producerGroup;
    private String namesrvAddr;
    private String mqTopic;
  }

}
