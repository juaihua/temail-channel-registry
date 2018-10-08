package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServerTimer {

  private final HashedWheelTimer heartBeatTimer = new HashedWheelTimer();

  private final Map<String, Timeout> serverTimeout = new ConcurrentHashMap<>();

  private Consumer consumer;

  //heart timeout 30 seconds
  private final long delay = 30;

  private TemailAcctStsService temailAcctStsService;

  public GrpcServerTimer(TemailAcctStsService temailAcctStsService, Consumer consumer) {
    this.consumer = consumer;
    this.temailAcctStsService = temailAcctStsService;
  }

  private String extractHashKey(GatewayServer gatewayServer) {
    return gatewayServer.getIp() + "-" + gatewayServer.getProcessId();
  }

  public void addHeartBeatTimeout(GatewayServer gatewayServer) {
    //log.info("gateway server : {}-{} report heartBeat ",
      //  gatewayServer.getIp(), gatewayServer.getProcessId());
    //if already exists, invalid the old task
    String serverKey = extractHashKey(gatewayServer);
    Optional.ofNullable(serverTimeout.get(serverKey))
        .ifPresent(timeout -> {
          log.debug("remove recently timeout task of : {}-{} ",
              gatewayServer.getIp(), gatewayServer.getProcessId());
          timeout.cancel();
        });
    Timeout timeout = heartBeatTimer.newTimeout(new
            GrpcServerTimerTask(temailAcctStsService, gatewayServer, consumer),
        delay, TimeUnit.SECONDS);
    serverTimeout.put(serverKey, timeout);
    log.debug("add new timeout task for gatewayserver :{}-{} ",
        gatewayServer.getIp(), gatewayServer.getProcessId());

    //TODO
    //if grpc client reconnect to another channel server in a short time
    //cause of network error,  the original channel server will offLine
    //the gateway server by mistake, so we need to check the offLine list
    temailAcctStsService.fixPotentialMiskakeOffLine(
        new CdtpServer(
          gatewayServer.getIp(),
          gatewayServer.getProcessId(),
          gatewayServer.getCurStateBeginTime(),
            null));
  }
}
