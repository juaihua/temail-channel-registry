package com.syswin.temail.channel.grpc.servers;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
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
 * grpc bean should be transformed to json, then wen can update some filelds, like this:
 *  GatewayServer gateServer = GatewayServer.newBuilder().setCurStateBeginTime("1980-06-09")
 *       .setGatewayServerState(GatewayServerState.offLine)
 *       .setIp("192.168.197.1").setProcessId("aspdiqee8280aud0fja").build();
 *  String jsonStr = JsonFormat.printer().print(gateServer);
 *  Gson gson = Gson.fromJson(jsonStr)
 *  gosn.set(xx, xx)
 *  but to much complex, so we keep the original beans .
 */
@Slf4j
public class GrpcServerImpl extends GatewayRegistrySyncServerGrpc.GatewayRegistrySyncServerImplBase {

  private TemailAcctStsService temailAcctStsService;

  private GrpcServerTimer grpcServerTimer;

  public GrpcServerImpl(TemailAcctStsService temailAcctStsService) {
    this.temailAcctStsService = temailAcctStsService;
    this.grpcServerTimer = new GrpcServerTimer(temailAcctStsService, null);
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
   * build List<TemailAcctSts> from ChannelLocationes
   *
   * @param channelLocationes
   * @return
   */
  private List<TemailAcctSts> transform2TemailAcctStses(ChannelLocationes channelLocationes) {
    List<TemailAcctSts> acctStses = new ArrayList<TemailAcctSts>();
    channelLocationes.getChannelLocationListList().forEach(channelLocation -> {
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
      CdtpServer cdtpServer = new CdtpServer(ip,processId,curStateBeginTime,null);
      this.temailAcctStsService.registerOrRecorveryServer(cdtpServer);
      this.grpcServerTimer.addHeartBeatTimeout(gatewayServer);
      log.info("{} registry server success.", gatewayServer.toString());
      commonResponse = this.buildCommonResponse(true, "ok", null);
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage(), null);
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
      commonResponse = this.buildCommonResponse(true, "ok", null);
      log.info("{} heart beat success.", gatewayServer.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage(), null);
      log.error("server heartBeat fail. ", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * handle channelLocationes sync request
   *
   * @param channelLocationes
   * @param responseObserver
   */
  @Override
  public void syncChannelLocationes(ChannelLocationes channelLocationes,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      List<TemailAcctSts> acctStses = transform2TemailAcctStses(channelLocationes);
      this.temailAcctStsService.addStatus(new TemailAcctStses(acctStses));
      commonResponse = this.buildCommonResponse(true, "ok", null);
      log.info("{} sync channel locationes success.", channelLocationes.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage(), null);
      log.error("sync channel locationes fail . ", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * handle channelLocationes remove request
   *
   * @param channelLocationes
   * @param responseObserver
   */
  @Override
  public void removeChannelLocationes(ChannelLocationes channelLocationes,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      this.temailAcctStsService.delStatus(
          new TemailAcctStses(this.transform2TemailAcctStses(channelLocationes)));
      commonResponse = this.buildCommonResponse(true, "ok", null);
      log.info("{} remove channel locations success.", channelLocationes.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage(), null);
      log.error("remove channel locationes fail .", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  @Override
  public void getChannelLocationesByAccount(UserAccount request,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      ChannelLocationes.Builder builder = ChannelLocationes.newBuilder();
      TemailAcctStses temailAcctStses = this.temailAcctStsService.locateStatus(request.getAccount());
      temailAcctStses.getStatuses().forEach(t -> {
            builder.addChannelLocationList(
                ChannelLocation.newBuilder().setAccount(t.getAccount()).setDevId(t.getDevId()).setHostOf(t.getHostOf())
                    .setProcessId(t.getProcessId()).setMqTopic(t.getMqTopic()).setMqTag(t.getMqTag()));
          }
      );
      ChannelLocationes channelLocationes = builder.build();
      commonResponse = this.buildCommonResponse(true, "ok", channelLocationes);
      log.info("query channel locationes by {} success, response {}", request.getAccount(), channelLocationes.toString());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, "ok", null);
      log.error("get channel locationes by account fail .", e);
    }
    this.commonResponse(responseObserver, commonResponse);
  }


  public CommonResponse buildCommonResponse(boolean
      isSuccess, String msg, Message data) {
    if (data != null) {
      return CommonResponse.newBuilder().setIsSuccess(isSuccess)
          .setMsg(msg).setRespData(Any.pack(data)).build();
    } else {
      return CommonResponse.newBuilder().setIsSuccess(isSuccess)
          .setMsg(msg).build();
    }
  }

}
