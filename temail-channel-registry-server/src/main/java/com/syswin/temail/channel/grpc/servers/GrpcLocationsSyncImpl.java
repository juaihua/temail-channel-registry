package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * grpc server implmentation
 */
@Slf4j
public class GrpcLocationsSyncImpl extends GatewayRegistrySyncServerGrpc.GatewayRegistrySyncServerImplBase {

  private final TemailAcctStsService temailAcctStsService;

  private final GrpcServerTimer grpcServerTimer;

  public GrpcLocationsSyncImpl(TemailAcctStsService temailAcctStsService) {
    this.temailAcctStsService = temailAcctStsService;
    grpcServerTimer = new GrpcServerTimer(temailAcctStsService, t -> {
    });
  }


  /**
   * response a common data
   *
   * @param responseObserver
   * @param commonResponse
   */
  private void commonResponse(StreamObserver<CommonResponse>
      responseObserver, CommonResponse commonResponse) {
    //response result
    try {
      responseObserver.onNext(commonResponse);
      responseObserver.onCompleted();
      log.debug("response grpc call result: {}", commonResponse.toString());
    } catch (Exception e) {
      responseObserver.onError(Status.INTERNAL.withCause(e)
          .withDescription("error").asRuntimeException());
      log.error("response grpc call result fail : {}");
    }
  }


  /**
   * build List<TemailAcctSts> from ChannelLocations
   *
   * @param channelLocations
   * @return
   */
  private List<TemailAcctSts> transform2TemailAcctStses(ChannelLocations channelLocations) {
    List<TemailAcctSts> acctStses = new ArrayList<TemailAcctSts>();
    channelLocations.getChannelLocationListList().forEach(channelLocation -> {
      acctStses.add(
          new TemailAcctSts(channelLocation.getAccount(), channelLocation.getDevId(),
              channelLocation.getHostOf(), channelLocation.getProcessId(),
              channelLocation.getMqTopic(), channelLocation.getMqTag()));
    });
    return acctStses;
  }


  /**
   * handle a registry request
   *
   * @param gatewayServer
   * @param responseObserver
   */
  @Override
  public void serverRegistry(GatewayServer gatewayServer,
      StreamObserver<CommonResponse> responseObserver) {
    // regist the server and add a timeout for heartBeat
    CommonResponse commonResponse = null;
    try {
      String ip = gatewayServer.getIp();
      String processId = gatewayServer.getProcessId();
      String curStateBeginTime = gatewayServer.getCurStateBeginTime();
      CdtpServer cdtpServer = new CdtpServer(ip, processId, curStateBeginTime, null);
      this.temailAcctStsService.registerOrRecorveryServer(cdtpServer);
      grpcServerTimer.addHeartBeatTimeout(gatewayServer);
      log.info("{} registry server success.", gatewayServer.toString());
      commonResponse = this.buildCommonResponse(true, "ok");
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("server register fail : {}", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * handle heartbeat request
   *
   * @param gatewayServer
   * @param responseObserver
   */
  @Override
        public void serverHeartBeat(GatewayServer gatewayServer,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      grpcServerTimer.addHeartBeatTimeout(gatewayServer);
      commonResponse = this.buildCommonResponse(true, "ok");
      log.info("{} heart beat success.", gatewayServer.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("server heartBeat fail. ", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * handle channelLocations sync request
   *
   * @param channelLocations
   * @param responseObserver
   */
  @Override
  public void syncChannelLocations(ChannelLocations channelLocations,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      List<TemailAcctSts> acctStses = transform2TemailAcctStses(channelLocations);
      this.temailAcctStsService.addStatus(new TemailAcctStses(acctStses));
      commonResponse = this.buildCommonResponse(true, "ok");
      log.info("{} sync channel Locations success.", channelLocations.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("sync channel Locations fail . ", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * handle channelLocations remove request
   *
   * @param channelLocations
   * @param responseObserver
   */
  @Override
  public void removeChannelLocations(ChannelLocations channelLocations,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      this.temailAcctStsService.delStatus(
          new TemailAcctStses(this.transform2TemailAcctStses(channelLocations)));
      commonResponse = this.buildCommonResponse(true, "ok");
      log.info("{} remove channel locations success.", channelLocations.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("remove channel Locations fail .", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  public CommonResponse buildCommonResponse(boolean
      isSuccess, String msg) {
    return CommonResponse.newBuilder().setIsSuccess(isSuccess)
        .setMsg(msg).build();
  }
}
