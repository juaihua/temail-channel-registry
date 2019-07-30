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
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import com.syswin.temail.channel.loginhistory.LoginHistoryRunner;
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

  private final LoginHistoryRunner loginHistoryRunner;

  public GrpcLocationsSyncImpl(TemailAcctStsService temailAcctStsService,
      LoginHistoryRunner loginHistoryRunner) {
    this.temailAcctStsService = temailAcctStsService;
    this.loginHistoryRunner = loginHistoryRunner;
    this.grpcServerTimer = new GrpcServerTimer(temailAcctStsService);
  }


  /**
   * response a common data
   */
  private void commonResponse(StreamObserver<CommonResponse>
      responseObserver, CommonResponse commonResponse) {
    //response result
    try {
      responseObserver.onNext(commonResponse);
      responseObserver.onCompleted();
      log.info("response grpc call result: {}", commonResponse.toString());
    } catch (Exception e) {
      log.error("grpc server response fail. ", e);
    }
  }


  /**
   * build List<TemailAcctSts> from ChannelLocations
   */
  private List<TemailAcctSts> transform2TemailAcctStses(ChannelLocations channelLocations) {
    List<TemailAcctSts> acctStses = new ArrayList<TemailAcctSts>();
    channelLocations.getChannelLocationListList().forEach(channelLocation -> {
      acctStses.add(
          new TemailAcctSts(channelLocation.getAccount(), channelLocation.getDevId(), channelLocation.getPlatform(),
              channelLocation.getAppVer(),
              channelLocation.getHostOf(), channelLocation.getProcessId(),
              channelLocation.getMqTopic(), channelLocation.getMqTag()));
    });
    return acctStses;
  }


  /**
   * handle a registry request
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
      log.info("{} {} registry server success.", gatewayServer.getIp(), gatewayServer.getProcessId());
      commonResponse = this.buildCommonResponse(true, "ok");
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("server register fail : {}", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * server offLine notified by gateway server
   */
  @Override
  public void serverOffLine(GatewayServer gatewayServer, StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      grpcServerTimer.offLineServer(gatewayServer);
      commonResponse = this.buildCommonResponse(true, "ok");
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("server offLine fail : {}", e);
    }
    commonResponse(responseObserver, commonResponse);

  }

  /**
   * handle heartbeat request
   */
  @Override
  public void serverHeartBeat(GatewayServer gatewayServer,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      grpcServerTimer.addHeartBeatTimeout(gatewayServer);
      commonResponse = this.buildCommonResponse(true, "ok");
      log.info("{} {} heart beat handle success.", gatewayServer.getIp(), gatewayServer.getProcessId());
    } catch (Exception e) {
      commonResponse = this.buildCommonResponse(false, e.getMessage());
      log.error("server heartBeat fail. ", e);
    }
    commonResponse(responseObserver, commonResponse);
  }


  /**
   * handle channelLocations sync request
   */
  @Override
  public void syncChannelLocations(ChannelLocations channelLocations,
      StreamObserver<CommonResponse> responseObserver) {
    CommonResponse commonResponse = null;
    try {
      List<TemailAcctSts> acctStses = transform2TemailAcctStses(channelLocations);
      this.temailAcctStsService.addStatus(new TemailAcctStses(acctStses));
      this.loginHistoryRunner.persistAcctStses(new TemailAcctStses(acctStses));
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
