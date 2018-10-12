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
