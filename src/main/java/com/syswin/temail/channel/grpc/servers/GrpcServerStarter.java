/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.service.TemailAcctStsService;
import com.syswin.temail.channel.loginhistory.LoginHistoryRunner;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerStarter {

  private Server server;

  private final TemailAcctStsService temailAcctStsService;

  private final LoginHistoryRunner loginHistoryRunner;

  private final int port;

  public GrpcServerStarter(TemailAcctStsService temailAcctStsService, LoginHistoryRunner loginHistoryRunner,
      int port) {
    this.port = port;
    this.loginHistoryRunner = loginHistoryRunner;
    this.temailAcctStsService = temailAcctStsService;
  }

  /**
   * start server
   */
  public void start() throws IOException {
    //start grpc server and add a hook to clean resources when jvm close;
    server = ServerBuilder.forPort(port)
        .addService(new GrpcLocationsSyncImpl(temailAcctStsService, loginHistoryRunner))
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
