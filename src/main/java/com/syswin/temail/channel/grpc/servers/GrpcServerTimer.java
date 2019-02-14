package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerTimer {

  final Map<String, Timeout> serverTimeout = new ConcurrentHashMap<>();

  private final HashedWheelTimer heartBeatTimer = new HashedWheelTimer();

  private TemailAcctStsService temailAcctStsService;

  private final long delay = 30;

  public GrpcServerTimer(TemailAcctStsService temailAcctStsService) {
    this.temailAcctStsService = temailAcctStsService;
  }

  private String extractHashKey(GatewayServer gatewayServer) {
    return gatewayServer.getIp() + "-" + gatewayServer.getProcessId();
  }

  void addHeartBeatTimeout(GatewayServer gatewayServer) {
    //remove old timeout task if exits
    String serverKey = extractHashKey(gatewayServer);
    Optional.ofNullable(serverTimeout.get(serverKey)).ifPresent(timeout -> {
      log.info("remove recently timeout task of : {}-{} ",
          gatewayServer.getIp(), gatewayServer.getProcessId());
      timeout.cancel();
    });

    //add new timeout task
    Timeout timeout = heartBeatTimer
        .newTimeout(new GrpcServerTimerTask<GatewayServer>(temailAcctStsService, gatewayServer,
            t -> {
              //remove gateway server from memory
              serverTimeout.remove(extractHashKey(t));
            }), delay, TimeUnit.SECONDS);
    serverTimeout.put(serverKey, timeout);
    log.info("add new timeout task for gatewayserver :{}-{} ",
        gatewayServer.getIp(), gatewayServer.getProcessId());

    //fix potential offLine action by mistake
    temailAcctStsService.fixPotentialMiskakeOffLine(
        new CdtpServer(
            gatewayServer.getIp(),
            gatewayServer.getProcessId(),
            gatewayServer.getCurStateBeginTime(),
            null));
  }

  boolean offLineServer(GatewayServer gatewayServer) throws Exception {
    Timeout timeout = serverTimeout.get(extractHashKey(gatewayServer));
    if (timeout != null) {
      timeout.task().run(timeout);
      timeout.cancel();
    }
    return true;
  }

}
