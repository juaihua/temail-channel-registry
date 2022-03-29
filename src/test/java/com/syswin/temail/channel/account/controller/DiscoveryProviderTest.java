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

package com.syswin.temail.channel.account.controller;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

//@RunWith(SpringRestPactRunner.class)
@ActiveProfiles("dev")
//@PactBroker(host = "172.28.50.206", port = "88")
//@Provider("temail-discovery")
@SpringBootTest(webEnvironment = DEFINED_PORT, properties = "server.port=9100")
@Ignore
public class DiscoveryProviderTest {
  //
  //@TestTarget
  //public final Target target = new HttpTarget(9100);
  //private final String sean = "sean@t.email";
  //private final String jack = "jack@t.email";
  //
  //@MockBean
  //private TemailAcctStsService connectionStatusService;
  //
  //@State("New connection")
  //public void newConnection() {
  //  doAnswer(this::throwIfArgumentMismatch).when(connectionStatusService).addStatus(any());
  //}
  //
  //@State("Remove connection")
  //public void removeConnection() {
  //  doAnswer(this::throwIfArgumentMismatch).when(connectionStatusService).delStatus(any());
  //}
  //
  //@State({"Locate connection", "Remote discovery service error"})
  //public void locateConnection() {
  //  when(connectionStatusService.locateStatus(sean)).thenReturn(new TemailAcctStses(singletonList(location())));
  //  when(connectionStatusService.locateStatus(jack)).thenThrow(new RuntimeException("oops"));
  //}
  //
  //private TemailAcctSts location() {
  //  return new TemailAcctSts(
  //      sean,
  //      "iOS-sean",
  //      "pc",
  //      "2.5.0",
  //      "localhost",
  //      "12345",
  //      "temail-gateway",
  //      "gateway-localhost"
  //  );
  //}
  //
  //private Object throwIfArgumentMismatch(InvocationOnMock invocationOnMock) {
  //  TemailAcctStses argument = invocationOnMock.getArgument(0);
  //  TemailAcctSts temailAcctSts = argument.getStatuses().get(0);
  //
  //  boolean matched = sean.equals(temailAcctSts.getAccount()) &&
  //      "iOS-sean".equals(temailAcctSts.getDevId()) &&
  //      "localhost".equals(temailAcctSts.getHostOf()) &&
  //      "12345".equals(temailAcctSts.getProcessId()) &&
  //      "temail-gateway".equals(temailAcctSts.getMqTopic()) &&
  //      "gateway-localhost".equals(temailAcctSts.getMqTag());
  //
  //  if (matched) {
  //    return null;
  //  }
  //
  //  throw new RuntimeException("unmatched call");
  //}
}