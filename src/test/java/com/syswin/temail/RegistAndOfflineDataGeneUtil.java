package com.syswin.temail;

import com.syswin.temail.beans.CdtpServer;
import com.syswin.temail.beans.TemailAccountStatus;
import com.syswin.temail.beans.TemailAccountStatusUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by juaihua on 2018/8/22.
 * 100 server instances and 1000000 userChannels
 */
public class RegistAndOfflineDataGeneUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistAndOfflineDataGeneUtil.class);

    public static List<TemailAccountStatusUpdateRequest> temailAccountStatusUpdateRequests = new ArrayList<TemailAccountStatusUpdateRequest>();

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
            status.setDevId(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.NUM,10));
            status.setProcessId(cdtpServer.getProcessId());
            status.setMqTag(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER,4));
            request.setStatus(status);
            request.setAccount(CommonDataGeneUtil.extractChar(CommonDataGeneUtil.ExtractType.LOWER,4)+"@temail.com");
            request.setOptype(TemailAccountStatusUpdateRequest.OptType.add);
            temailAccountStatusUpdateRequests.add(request);
        }
    }

    public static void main(String[] args) {}
}
