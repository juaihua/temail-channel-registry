package com.syswin.temail.channel.account;

import com.google.gson.Gson;
import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.TemailAccountStatus;
import com.syswin.temail.channel.account.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.channel.account.service.ConnectionStatusServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CdtpStatusApplicationTests {

    private static final Random RANDOM = new Random();

  private static final Logger LOGGER = LoggerFactory.getLogger(CdtpStatusApplicationTests.class);

    static {RegistAndOfflineDataGeneUtil.buildTestDatas(10000);}

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * test update channel status
     */
    //@Test
    public void testUpdateStatus4Update(){
        try {
            TemailAccountStatusUpdateRequest request = new TemailAccountStatusUpdateRequest();
            request.setAccount("temailStatusTestaAcct@temail.com");
            request.setStatus(new TemailAccountStatus("TestDevId-1","192.168.197.30","9812","mqTopci","mqTag"));
            request.setOptype(TemailAccountStatusUpdateRequest.OptType.add);
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/updateStatus").contentType("application/json;charset=utf-8").content(new Gson().toJson(request)))
                     .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
            LOGGER.info(" get result from updateStatus 4 add : {}",result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * test update channel status
     */
    //@Test
    public void testUpdateStatus4Delete(){
        try {
            TemailAccountStatusUpdateRequest request = new TemailAccountStatusUpdateRequest();
            request.setAccount("temailStatusTestaAcct@temail.com");
            request.setStatus(new TemailAccountStatus("TestDevId-1","192.168.197.30","9812","mqTopci","mqTag"));
            request.setOptype(TemailAccountStatusUpdateRequest.OptType.del);
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/updateStatus").contentType("application/json;charset=utf-8").content(new Gson().toJson(request)))
                     .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
            LOGGER.info(" get result from updateStatus 4 delete : {}",result.getResponse().getContentAsString());
            LOGGER.info(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testLocateStatus(){
        try {
            MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.get("/locateStatus/temailStatusTestaAcct@temail.com"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content()
                            .contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
            LOGGER.info("get result from testLocateStatus : {}" ,result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * test register and offLine
      */
    @Test
    public void testServerRegistsAndOffLine(){
        ConnectionStatusServiceImpl connectionStatusService = webApplicationContext.getBean(ConnectionStatusServiceImpl.class);

        /*register servers to onLineServers*/
        for(CdtpServer server : RegistAndOfflineDataGeneUtil.cdtpServers){
            connectionStatusService.registerOrRecorveryServer(server);
        }

        /*offLine serveal sververs, may be 0.2 */
        for(CdtpServer server : RegistAndOfflineDataGeneUtil.cdtpServers){
            if(RANDOM.nextDouble() < 0.2 ) connectionStatusService.offLineTheServer(server);
        }

        ///*register connections*/
        //for(TemailAccountStatusUpdateRequest request : RegistAndOfflineDataGeneUtil.temailAccountStatusUpdateRequests){
        //  connectionStatusService.updateStatus(request);
        //}

    }
}
