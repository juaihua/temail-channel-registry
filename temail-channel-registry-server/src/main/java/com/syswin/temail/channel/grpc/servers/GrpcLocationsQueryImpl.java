package com.syswin.temail.channel.grpc.servers;

import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * grpc server implmentation
 */
@Slf4j
public class GrpcLocationsQueryImpl extends DispatchLocationQueryServerGrpc.DispatchLocationQueryServerImplBase {

  private final TemailAcctStsService temailAcctStsService;

  public GrpcLocationsQueryImpl(TemailAcctStsService temailAcctStsService) {
    this.temailAcctStsService = temailAcctStsService;
  }

  @Override
  public void getChannelLocationsByAccount(UserAccount request,
      StreamObserver<ChannelLocations> responseObserver) {
    try {
      ChannelLocations.Builder builder = ChannelLocations.newBuilder();
      TemailAcctStses temailAcctStses = this.temailAcctStsService.locateStatus(request.getAccount());
      temailAcctStses.getStatuses().forEach(t -> {
            builder.addChannelLocationList(
                ChannelLocation.newBuilder().setAccount(t.getAccount()).setDevId(t.getDevId()).setHostOf(t.getHostOf())
                    .setProcessId(t.getProcessId()).setMqTopic(t.getMqTopic()).setMqTag(t.getMqTag()));
          }
      );
      ChannelLocations channelLocations = builder.build();
      log.info("query channel Locations by {} success, response {}", request.getAccount(), channelLocations.toString());
      responseObserver.onNext(channelLocations);
      responseObserver.onCompleted();
    } catch (Exception e) {
      log.error("grpc server response fail. ", e);
    }
  }
}
