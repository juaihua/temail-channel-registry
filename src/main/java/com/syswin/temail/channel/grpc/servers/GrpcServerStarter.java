package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerStarter {

  private Server server;

  private final TemailAcctStsService temailAcctStsService;

  private final int port;

  public GrpcServerStarter(TemailAcctStsService temailAcctStsService, int port) {
    this.port = port;
    this.temailAcctStsService = temailAcctStsService;
  }

  /**
   * start server
   */
  public void start() throws IOException {
    //start grpc server and add a hook to clean resources when jvm close;
    server = ServerBuilder.forPort(port)
        .addService(new GrpcLocationsSyncImpl(temailAcctStsService))
        .addService(new GrpcLocationsQueryImpl(temailAcctStsService))
        .build().start();
    log.info("grpc server start successfully.");
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        log.error("**** shutting down grpc server since JVM js shutting down ****");
        GrpcServerStarter.this.stop();
      }
    });
  }


  /**
   * stop server
   */
  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }


  /**
   * used for test execute
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

}
