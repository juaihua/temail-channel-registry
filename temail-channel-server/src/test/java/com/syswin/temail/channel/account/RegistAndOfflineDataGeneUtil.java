package com.syswin.temail.channel.account;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.TemailAccountStatus;
import com.syswin.temail.channel.account.beans.TemailAccountStatusUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juaihua on 2018/8/22. 100 server instances and 1000000 userChannels
 */
public class RegistAndOfflineDataGeneUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegistAndOfflineDataGeneUtil.class);
  private static final Random RANDOM = new Random(1);
  public static List<TemailAccountStatusUpdateRequest> temailAccountStatusUpdateRequests;
  public static List<CdtpServer> cdtpServers;
  private static String[] ips = new String[30];
  private static String[] processIds = new String[30];

  static {
    for (int i = 0; i < 30; i++) {
      ips[i] = "192.168.197." + i;
      processIds[i] = 9000 + i + "";
    }
  }

  public static void buildTestDatas(int size) {
    cdtpServers = new ArrayList<CdtpServer>(size);
    temailAccountStatusUpdateRequests = new ArrayList<TemailAccountStatusUpdateRequest>(size);
    for (int i = 0; i < size; i++) {
      //gene CdtpServer
      int hostAndProcessIndex = RANDOM.nextInt(ips.length);
      CdtpServer cdtpServer = new CdtpServer();
      cdtpServer.setIp(ips[hostAndProcessIndex]);
      cdtpServer.setProcessId(processIds[hostAndProcessIndex]);
      cdtpServers.add(cdtpServer);

      TemailAccountStatusUpdateRequest request = new TemailAccountStatusUpdateRequest();
      TemailAccountStatus status = new TemailAccountStatus();
      status.setMqTopic(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER, 8));
      status.setHostOf(cdtpServer.getIp());
      status.setDevId(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.NUM, 10));
      status.setProcessId(cdtpServer.getProcessId());
      status.setMqTag(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER, 4));
      request.setStatus(status);
      request.setAccount(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER, 4) + "@temail.com");
      request.setOptype(TemailAccountStatusUpdateRequest.OptType.add);
      temailAccountStatusUpdateRequests.add(request);
    }
  }

  public static void main(String[] args) {
  }
}
