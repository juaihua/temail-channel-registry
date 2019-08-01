package com.syswin.temail.channel.loginhistory;

import com.google.gson.Gson;
import com.syswin.temail.channel.account.beans.LoginHistory;
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;

@Slf4j
public class LoginHistoryRunner implements CommandLineRunner {

  private final BlockingQueue<TemailAcctStses> blockingQueue = new LinkedBlockingQueue<>();

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  private MQProducer mqProducer;

  private String mqTopic;

  public LoginHistoryRunner(MQProducer mqProducer, String mqTopic) {
    this.mqTopic = mqTopic;
    this.mqProducer = mqProducer;
  }

  @Override
  public void run(String... args) throws Exception {
    this.executorService.submit(this::sendToMqThread);
  }

  private void sendToMqThread() {
    log.info("Ready to start deal login record ... ");
    TemailAcctStses temailAcctStses = null;
    while (!Thread.currentThread().isInterrupted()) {
      try {
        temailAcctStses = LoginHistoryRunner.this.blockingQueue.take();
        doPersistLocations(temailAcctStses);
      } catch (InterruptedException e) {
        log.error("InterruptedException was caught, exit task executing loop!", e);
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        log.warn("failed to deal login history ", e);
      }
    }
  }

  /**
   * persist login info to mq
   */
  public void doPersistLocations(final TemailAcctStses temailAcctStses) {
    Gson gson = new Gson();
    List<Message> messages = new ArrayList<>();
    try {
      temailAcctStses.getStatuses().forEach(acctSts -> {
        LoginHistory loginHistory = transAcctStatusToLoginHistory(acctSts);
        if (isNeedPersist(loginHistory)) {
          String jsonStr = gson.toJson(loginHistory);
          Message message = new Message(mqTopic, "tagB",
              jsonStr.getBytes(StandardCharsets.UTF_8));
          messages.add(message);
        }
      });
      //make mq message and send
      log.info("send login info to mq {}", temailAcctStses);
      mqProducer.send(messages);
    } catch (Exception e) {
      log.info("persist login info error,login info is {}", temailAcctStses, e);
    }
  }

  private static LoginHistory transAcctStatusToLoginHistory(TemailAcctSts temailAcctSts) {
    LoginHistory loginRecord = new LoginHistory();
    BeanUtils.copyProperties(temailAcctSts, loginRecord);
    loginRecord.setLoginTime(new Date());
    return loginRecord;
  }


  public void persistAcctStses(TemailAcctStses stses) {
    this.blockingQueue.offer(stses);
  }

  /**
   * persist the login only when the appVer is not null
   */
  private boolean isNeedPersist(LoginHistory loginHistory) {
    return StringUtils.isNotEmpty(loginHistory.getAppVer());
  }

}