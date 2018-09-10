package com.syswin.temail.channel.account.controller;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRestPactRunner.class)
@PactBroker(host = "172.28.50.206", port = "88")
@Provider("temail-discovery")
@SpringBootTest(webEnvironment = DEFINED_PORT, properties = "server.port=8081")
public class DiscoveryProviderTest {
  @TestTarget
  public final Target target = new HttpTarget(8081);
  private final String sean = "sean@t.email";
  private final String jack = "jack@t.email";

  @MockBean
  private TemailAcctStsService connectionStatusService;

  @State("New connection")
  public void newConnection() {
    doAnswer(this::throwIfArgumentMismatch).when(connectionStatusService).addStatus(any());
  }

  @State("Remove connection")
  public void removeConnection() {
    doAnswer(this::throwIfArgumentMismatch).when(connectionStatusService).delStatus(any());
  }

  @State({"Locate connection", "Remote discovery service error"})
  public void locateConnection() {
    when(connectionStatusService.locateStatus(sean)).thenReturn(new TemailAcctStses(singletonList(location())));
    when(connectionStatusService.locateStatus(jack)).thenThrow(new RuntimeException("oops"));
  }

  private TemailAcctSts location() {
    return new TemailAcctSts(
        sean,
        "iOS-sean",
        "localhost",
        "12345",
        "temail-gateway",
        "gateway-localhost"
    );
  }

  private Object throwIfArgumentMismatch(InvocationOnMock invocationOnMock) {
    TemailAcctStses argument = invocationOnMock.getArgument(0);
    TemailAcctSts temailAcctSts = argument.getStatuses().get(0);

    boolean matched = sean.equals(temailAcctSts.getAccount()) &&
        "iOS-sean".equals(temailAcctSts.getDevId()) &&
        "localhost".equals(temailAcctSts.getHostOf()) &&
        "12345".equals(temailAcctSts.getProcessId()) &&
        "temail-gateway".equals(temailAcctSts.getMqTopic()) &&
        "gateway-localhost".equals(temailAcctSts.getMqTag());

    if (matched) {
      return null;
    }

    throw new RuntimeException("unmatched call");
  }
}