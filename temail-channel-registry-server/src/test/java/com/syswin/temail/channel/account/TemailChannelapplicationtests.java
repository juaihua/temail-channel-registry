package com.syswin.temail.channel.account;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;
import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT, properties = "server.port=9100")
@ActiveProfiles("dev")
public class TemailChannelapplicationtests {

  private static final Random RANDOM = new Random();

  private static final Logger LOGGER = LoggerFactory.getLogger(TemailChannelapplicationtests.class);

  static {
    RegistAndOfflineDataGeneUtil.buildTestDatas(2000);
  }

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  /**
   * test update channel status
   */
  @Test
  @Ignore
  public void test_1_AddSts() {
    try {
      TemailAcctStses request = new TemailAcctStses();
      request.setStatuses(new ArrayList<TemailAcctSts>() {{
        add(new TemailAcctSts("temailStatusTestaAcct@temail.com",
            "TestDevId-1", "192.168.197.38", "9812", "mqTopci", "mqTag"));
      }});
      MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/locations")
          .contentType("application/json;charset=utf-8")
          .content(new Gson().toJson(request)))
          .andExpect(MockMvcResultMatchers.status().is(new BaseMatcher<Integer>() {
            @Override
            public boolean matches(Object item) {
              return item.toString().equals(HttpStatus.OK.toString())
                  || item.toString().equals(HttpStatus.CREATED.toString());
            }

            @Override
            public void describeTo(Description description) {
              description.appendText("expected status is 200 or 201");
            }
          })).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
      LOGGER.info(" get result from updateStatus 4 add : {}", result.getResponse().getContentAsString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void test_2_LocateSts() {
    try {
      MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/locations/temailStatusTestaAcct@temail.com"))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
          .andReturn();
      LOGGER.info("get result from testLocateStatus : {}", result.getResponse().getContentAsString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * test update channel status
   */
  @Test
  @Ignore
  public void test_3_DelSts() {
    try {
      TemailAcctStses request = new TemailAcctStses();
      request.setStatuses(new ArrayList<TemailAcctSts>() {{
        add(new
            TemailAcctSts("temailStatusTestaAcct@temail.com",
            "TestDevId-1", "192.168.197.38", "9812", "mqTopci", "mqTag"));
      }});
      MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/locations")
          .contentType("application/json;charset=utf-8")
          .content(new Gson().toJson(request)))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
          .andReturn();
      LOGGER.info(" get result from updateStatus 4 delete : {}", result.getResponse().getContentAsString());
      LOGGER.info(result.getResponse().getContentAsString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * test register and offLine
   */
  @Test
  @Ignore
  public void test_4_geneRegisteredAndOffLinedServers() {
    TemailAcctStsService connectionStatusService = webApplicationContext.getBean(TemailAcctStsService.class);

    /*register servers to onLineServers*/
    for (CdtpServer server : RegistAndOfflineDataGeneUtil.cdtpServers) {
      connectionStatusService.registerOrRecorveryServer(server);
    }

    /*offLine serveal sververs, may be 0.2 */
    for (CdtpServer server : RegistAndOfflineDataGeneUtil.cdtpServers) {
      if (RANDOM.nextDouble() < 0.2) {
        connectionStatusService.offLineTheServer(server);
      }
    }

    /*register connections*/
    for (TemailAcctStses request : RegistAndOfflineDataGeneUtil.temailAccountStatusUpdateRequests) {
      connectionStatusService.addStatus(request);
    }
  }

}
