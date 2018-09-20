package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.ComnRespData;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerTimerTask implements TimerTask {

  private TemailAcctStsService temailAcctStsService;

  private GatewayServer gatewayServer;

  private Consumer consumer;

  public GrpcServerTimerTask(TemailAcctStsService temailAcctStsService,
      GatewayServer gatewayServer, Consumer consumer) {
    this.temailAcctStsService = temailAcctStsService;
    this.gatewayServer = gatewayServer;
    this.consumer = consumer;
  }

  @Override
  public void run(Timeout timeout) throws Exception {
    if (!timeout.isCancelled()) {
      log.warn("gateway server : {}-{} heartBeat timeout, offLine it. ", gatewayServer.getIp(),
          gatewayServer.getProcessId());
      ComnRespData comnRespData = temailAcctStsService.offLineTheServer(
          new CdtpServer(gatewayServer.getIp(), gatewayServer.getProcessId(),
              gatewayServer.getCurStateBeginTime(), null));
      //consumer for junit test to trace the servers that have been removed
      if (consumer != null) {
        consumer.accept(gatewayServer);
      }
    }
  }
}
