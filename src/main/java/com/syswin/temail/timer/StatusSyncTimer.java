package com.syswin.temail.timer;

import com.syswin.temail.beans.CdtpServer;
import com.syswin.temail.contants.RedisOptConstants;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.syswin.temail.contants.RedisOptConstants.CLEANING_SERVERS;
import static com.syswin.temail.contants.RedisOptConstants.OFFLINE_SERVERS;
import static com.syswin.temail.contants.RedisOptConstants.ONLINE_SERVERS;

/**
 * Created by juaihua on 2018/8/20.
 */
@Data
@Order(1)
@Component
public class StatusSyncTimer implements CommandLineRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusSyncTimer.class);

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Value("${statusKeepAlive.offLineServerTTL}")
    private Integer offLineServerTTL;

    @Value("${statusKeepAlive.handleMaxTime}")
    private Integer handleMaxTime;

    @Value("${statusKeepAlive.taskDelay}")
    private Integer taskDelay;

    @Value("${statusKeepAlive.initDelay}")
    private Integer initDelay;

    @Override
    public void run(String... strings) throws Exception {
        try {
            Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try{
                        CdtpServer offLineServer = (CdtpServer)(redisTemplate.opsForList().rightPopAndLeftPush(OFFLINE_SERVERS,OFFLINE_SERVERS));
                        if(!Optional.ofNullable(offLineServer).isPresent() || (RedisOptConstants.calicTimeCap(offLineServer.getCurStateBeginTime()) < offLineServerTTL)) return;
                        if(!redisTemplate.opsForHash().putIfAbsent(CLEANING_SERVERS,offLineServer.hashKey(),offLineServer)){
                            CdtpServer cleaningServer = (CdtpServer)(redisTemplate.opsForHash().get(CLEANING_SERVERS,offLineServer.hashKey()));
                            if((RedisOptConstants.calicTimeCap(cleaningServer.getCurStateBeginTime())) < handleMaxTime) return;
                        }
                        CdtpServer optServer = new CdtpServer();
                        optServer.setIp(offLineServer.getIp());optServer.setProcessId(offLineServer.getProcessId());
                        optServer.setCdtpServerState(CdtpServer.CdtpServerState.cleaning); optServer.setCurStateBeginTime(RedisOptConstants.format());
                        redisTemplate.opsForHash().put(ONLINE_SERVERS,optServer.hashKey(),optServer);
                        redisTemplate.opsForHash().put(CLEANING_SERVERS,optServer.hashKey(),optServer);

                        //clean invalid acct channel info
                        String keyPrefixPattern = RedisOptConstants.HOST_PREFIX_ON_REDIS + offLineServer.getIp() + "-" + offLineServer.getProcessId()+":*";
                        redisTemplate.keys(keyPrefixPattern).stream().forEach(acctkey -> {redisTemplate.opsForHash().keys(acctkey).stream().forEach(chanKey -> {
                            redisTemplate.opsForHash().delete(RedisOptConstants.TEMAIL_PREFIX_ON_REDIS + acctkey.toString().split(":")[3], chanKey);});});

                        //clean invalid hosts channel info
                        redisTemplate.delete(redisTemplate.keys(keyPrefixPattern));

                        //remove cdtpSever from onLineServer and offLineServer
                        redisTemplate.opsForHash().delete(ONLINE_SERVERS, optServer.hashKey());
                        redisTemplate.opsForList().remove(OFFLINE_SERVERS,0, offLineServer);
                        redisTemplate.opsForHash().delete(CLEANING_SERVERS, optServer.hashKey());

                    }catch(Exception e){
                        LOGGER.error("StatusSyncTimer error ...", e);
                    }
                }
            }, initDelay, taskDelay, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("StatusSyncTimer stopped ..", e);
        }
    }
}





