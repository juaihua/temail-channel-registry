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

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerTimerTask<T> implements TimerTask {

  private final TemailAcctStsService temailAcctStsService;

  private final GatewayServer gatewayServer;

  private final Consumer consumer;

  public GrpcServerTimerTask(TemailAcctStsService temailAcctStsService,
      GatewayServer gatewayServer, Consumer<T> consumer) {
    this.temailAcctStsService = temailAcctStsService;
    this.gatewayServer = gatewayServer;
    this.consumer = consumer;
  }

  @Override
  public void run(Timeout timeout) throws Exception {
    if (!timeout.isCancelled()) {
      log.warn("gateway server : {}-{} heartBeat timeout, offLine it. ",
          gatewayServer.getIp(), gatewayServer.getProcessId());
      //remove from redis
      temailAcctStsService.offLineTheServer(new CdtpServer(gatewayServer.getIp(),
          gatewayServer.getProcessId(), gatewayServer.getCurStateBeginTime(), null));
      if (consumer != null) {
        consumer.accept(gatewayServer);
      }
    }
  }
}
