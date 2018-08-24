package com.syswin.temail.channel.account;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.TemailAccountStatus;
import com.syswin.temail.channel.account.beans.TemailAccountStatusUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by juaihua on 2018/8/22.
 * 100 server instances and 1000000 userChannels
 */
@Slf4j
public class RegistAndOfflineDataGeneUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistAndOfflineDataGeneUtil.class);

    public static Set<TemailAccountStatusUpdateRequest> temailAccountStatusUpdateRequests = new HashSet<TemailAccountStatusUpdateRequest>();

    //public static Set<String> keys = new HashSet<TemailAccountStatusUpdateRequest>(String);

    public static List<CdtpServer> cdtpServers = new ArrayList<CdtpServer>();

    private static int serverInstances = 30;

    private static final Random RANDOM = new Random(1);

    static {
        for(int i = 0; i < serverInstances; i++){
            CdtpServer cdtpServer = new CdtpServer();
            cdtpServer.setIp("192.168.197." + i);
            cdtpServer.setProcessId(9000 + i +"");
            cdtpServers.add(cdtpServer);
        }
    }

    public static void buildTestDatas(int size){
        for(int i = 0; i < size; i++){
            int hostAndProcessIndex = RANDOM.nextInt(serverInstances);
            CdtpServer cdtpServer = cdtpServers.get(hostAndProcessIndex);
            TemailAccountStatusUpdateRequest request = new TemailAccountStatusUpdateRequest();
            TemailAccountStatus status = new TemailAccountStatus();
            status.setMqTopic(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER,8));
            status.setHostOf(cdtpServer.getIp());
            status.setDevId(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.NUM,15));
            status.setProcessId(cdtpServer.getProcessId());
            status.setMqTag(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER,10));
            request.setStatus(status);
            request.setAccount(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER,10)+"@temail.com");
            request.setOptype(TemailAccountStatusUpdateRequest.OptType.add);
            temailAccountStatusUpdateRequests.add(request);
        }
        log.debug("so now we have {} TemailAccountStatusUpdateRequest objects !", temailAccountStatusUpdateRequests.size());
        log.debug("and we have {} CdtpServer objects !", cdtpServers.size());
    }

    public static void main(String[] args) {}
}
