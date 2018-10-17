package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.service.TemailAcctStsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcServerManager implements ApplicationRunner {

  @Autowired
  private TemailAcctStsService temailAcctStsService;

  @Value("${app.channel.grpc.serverPort}")
  private String port;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("start grpc server...");
    new GrpcServerStarter(temailAcctStsService, Integer.parseInt(port)).start();
  }

}
