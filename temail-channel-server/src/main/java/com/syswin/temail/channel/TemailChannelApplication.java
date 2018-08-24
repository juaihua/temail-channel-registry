package com.syswin.temail.channel;

import com.syswin.temail.channel.connection.TemailChannelProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TemailChannelProperties.class)
public class TemailChannelApplication {

  public static void main(String[] args) {
    SpringApplication.run(TemailChannelApplication.class, args);
  }
}
