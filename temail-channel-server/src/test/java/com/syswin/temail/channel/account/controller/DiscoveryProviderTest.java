package com.syswin.temail.channel.account.controller;

import com.syswin.temail.channel.account.beans.ComnRespData;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRestPactRunner.class)
@PactBroker(host = "172.28.50.206", port = "88")
@Provider("temail-discovery")
@SpringBootTest(webEnvironment = DEFINED_PORT, properties = "server.port=8081")
public class DiscoveryProviderTest {
  @TestTarget
  public final Target target = new HttpTarget(8081);

  @MockBean
  private TemailAcctStsService connectionStatusService;

  @State("New connection")
  public void lookupUserBobDoesNotExist() {
    when(connectionStatusService.addStatus(any())).thenReturn(new ComnRespData(true));
  }

  //TODO 接着写删除和定位的验证！




}