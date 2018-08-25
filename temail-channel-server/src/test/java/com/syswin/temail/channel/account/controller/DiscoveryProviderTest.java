package com.syswin.temail.channel.account.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.syswin.temail.channel.account.beans.TemailAccountStatusUpdateResponse;
import com.syswin.temail.channel.account.service.ConnectionStatusServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRestPactRunner.class)
@PactBroker(host = "172.28.50.206", port = "88")
@Provider("temail-discovery")
@SpringBootTest(webEnvironment = DEFINED_PORT, properties = "server.port=8081")
public class DiscoveryProviderTest {
  @TestTarget
  public final Target target = new HttpTarget(8081);

  @MockBean
  private ConnectionStatusServiceImpl connectionStatusService;

  @State("New connection")
  public void lookupUserBobDoesNotExist() {
    when(connectionStatusService.updateStatus(any())).thenReturn(new TemailAccountStatusUpdateResponse(true));
  }

}