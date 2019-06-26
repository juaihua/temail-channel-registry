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

package com.syswin.temail.channel.account;

import java.util.*;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class RegistAndOfflineDataGeneUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegistAndOfflineDataGeneUtil.class);

  public static Set<TemailAcctStses> temailAccountStatusUpdateRequests = new HashSet<TemailAcctStses>();

  //public static Set<String> keys = new HashSet<TemailAccountStatusUpdateRequest>(String);

  public static List<CdtpServer> cdtpServers = new ArrayList<CdtpServer>();

  private static int serverInstances = 30;

  private static final Random RANDOM = new Random(1);

  static {
    for (int i = 0; i < serverInstances; i++) {
      CdtpServer cdtpServer = new CdtpServer();
      cdtpServer.setIp("192.168.197." + i);
      cdtpServer.setProcessId(9000 + i + "");
      cdtpServers.add(cdtpServer);
    }
  }

  public static void buildTestDatas(int size) {
    for (int i = 0; i < size; i++) {
      int hostAndProcessIndex = RANDOM.nextInt(serverInstances);
      CdtpServer cdtpServer = cdtpServers.get(hostAndProcessIndex);

      TemailAcctStses request = new TemailAcctStses(new ArrayList<TemailAcctSts>());
      for(int j = 0; j < RANDOM.nextInt(5)+1; j++){
        TemailAcctSts status = new TemailAcctSts();
        status.setMqTopic(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER, 8));
        status.setHostOf(cdtpServer.getIp());
        status.setDevId(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.NUM, 15));
        status.setProcessId(cdtpServer.getProcessId());
        status.setMqTag(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER, 10));
        status.setAccount(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER, 10) + "@temail.com");
        request.getStatuses().add(status);
      }
      temailAccountStatusUpdateRequests.add(request);
    }
    log.debug("so now we have {} TemailAccountStatusUpdateRequest objects !", temailAccountStatusUpdateRequests.size());
    log.debug("and we have {} CdtpServer objects !", cdtpServers.size());
  }


}
