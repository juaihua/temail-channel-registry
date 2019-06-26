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
