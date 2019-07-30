package com.syswin.temail.channel.mq;

import com.syswin.temail.channel.TemailChannelProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ChannelMQConfiguration {

  @Bean
  MQProducer producer(TemailChannelProperties properties) throws Exception {
    log.info("get rocket producer info is  is {}.", properties.getRocketmq());
    DefaultMQProducer producer = new DefaultMQProducer(properties.getRocketmq().getProducerGroup());
    producer.setNamesrvAddr(properties.getRocketmq().getNamesrvAddr());
    producer.start();
    return producer;
  }
}
