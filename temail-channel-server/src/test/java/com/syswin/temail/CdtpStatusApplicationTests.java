package com.syswin.temail;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CdtpStatusApplicationTests {

/*
    private static final Logger LOGGER = LoggerFactory.getLogger(CdtpStatusApplicationTests.class);


	@Test
	public void contextLoads() {}


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    */
/**
     * 测试状态更新
     *//*

    @Test
    public void testUpdateStatus(){
        try {
            TemailAccountStatusUpdateRequest request = new TemailAccountStatusUpdateRequest();
            request.setAccount("juaihua");
            request.setStatus(new TemailAccountStatus("1wd2e2e","12.24.34.23","topic1","2323"));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/updateStatus").content(new Gson().toJson(request)))
                     .andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                     .andReturn();

            LOGGER.info(result.getResponse().getContentAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testLocateStatus(){
        try {
            MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.get("/locateStatus/juaihua")).andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                     .andReturn();
            LOGGER.info(result.getResponse().getContentAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


*/
}
