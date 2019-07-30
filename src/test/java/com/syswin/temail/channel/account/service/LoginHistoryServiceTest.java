package com.syswin.temail.channel.account.service;

import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.loginhistory.LoginHistoryRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest(classes = TemailChannelApplication.class)
//@RunWith(SpringRunner.class)
public class LoginHistoryServiceTest {


  @Autowired
  private LoginHistoryRunner loginHistoryRunner;


  //  @Test
  public void testPersistLocations() throws Exception {

    List<TemailAcctSts> sts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      TemailAcctSts s = new TemailAcctSts("liuxing" + i, UUID.randomUUID().toString(), "mac", "2.5.0", "127.0.0.1",
          UUID.randomUUID().toString(), "topic", "tag");
      sts.add(s);
    }
    TemailAcctStses acctStses = new TemailAcctStses(sts);
    loginHistoryRunner.persistAcctStses(acctStses);
    Thread.sleep(2000);
  }
}
