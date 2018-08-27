package com.syswin.temail.channel.account.timer;

import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.contants.RedisOptConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.syswin.temail.channel.account.contants.RedisOptConstants.*;

/**
 * Created by juaihua on 2018/8/20.
 */
@Slf4j
@Data
@Order(1)
@Component
public class StatusSyncTimer implements CommandLineRunner {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

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



    // TODO 测试完后删掉
    offLineServerTTL = 10000;
    handleMaxTime = 20000;



    try {
      Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
        @Override
        public void run() {
          try {
            CdtpServer offLineServer = (CdtpServer) (redisTemplate.opsForList()
                .rightPopAndLeftPush(OFFLINE_SERVERS, OFFLINE_SERVERS));
            if (!Optional.ofNullable(offLineServer).isPresent()) {
              log.info("there is not any offLine server now, return .");
              return;
            }
            if ((RedisOptConstants.calicTimeCap(offLineServer.getCurStateBeginTime()) < offLineServerTTL)) {
              log.info("offLine server : {} not reach {} minutes , return .", offLineServer.toString(),
                  (offLineServerTTL / (1000 * 60)));
              return;
            }
            if (!redisTemplate.opsForHash().putIfAbsent(CLEANING_SERVERS, offLineServer.hashKey(), offLineServer)) {
              CdtpServer cleaningServer = (CdtpServer) (redisTemplate.opsForHash()
                  .get(CLEANING_SERVERS, offLineServer.hashKey()));
              log.info("the offLine server {} is now being handeled by other scheduled task now, return . ",
                  offLineServer.toString());
              if ((RedisOptConstants.calicTimeCap(cleaningServer.getCurStateBeginTime())) < handleMaxTime) {
                log.debug("and the time is not reach handleMaxTime {}, so we return . ", (handleMaxTime / (1000 * 60)),
                    offLineServer.toString());
                return;
              }
            }
            log.info("more than {} minutes passed , there seems to be some problems in other server , "
                    + "so now i will continue to handle this offLineserver {}. ", (handleMaxTime / (1000 * 60)),
                offLineServer.toString());
            CdtpServer optServer = new CdtpServer();
            optServer.setIp(offLineServer.getIp());
            optServer.setProcessId(offLineServer.getProcessId());
            optServer.setCdtpServerState(CdtpServer.CdtpServerState.cleaning);
            optServer.setCurStateBeginTime(RedisOptConstants.format());
            redisTemplate.opsForHash().put(ONLINE_SERVERS, optServer.hashKey(), optServer);
            redisTemplate.opsForHash().put(CLEANING_SERVERS, optServer.hashKey(), optServer);

            log.info("i will handle offLine server : {}", offLineServer.toString());
            String keyPrefixPattern =
                RedisOptConstants.HOST_PREFIX_ON_REDIS + offLineServer.getIp() + "-" + offLineServer.getProcessId()
                    + ":*";
            log.info("offLine server keyPrefixPattern is : {}", keyPrefixPattern);
            redisTemplate.keys(keyPrefixPattern).stream().forEach(acctkey -> {
              log.debug("remove {} info .", acctkey);
              redisTemplate.opsForHash().keys(acctkey).stream().forEach(chanKey -> {
                String delTemailAcctInfo = new StringBuilder(RedisOptConstants.TEMAIL_PREFIX_ON_REDIS)
                    .append(acctkey.substring(acctkey.lastIndexOf(":") + 1)).toString();
                redisTemplate.opsForHash().delete(delTemailAcctInfo,chanKey);
                log.debug("delete {} successfuly. ", delTemailAcctInfo);
              });
            });
            log.info("delete client connections from temal:status:* successfuly. ");

            //clean invalid hosts channel info
            Set<String> keysInHosts = redisTemplate.keys(keyPrefixPattern);
            redisTemplate.delete(keysInHosts);
            log.info("delete {} successfuly",keysInHosts.toString());
            log.info("delete client connections from temail:hosts: queue successfuly. ");

            //remove cdtpSever from onLineServer and offLineServer
            redisTemplate.opsForHash().delete(ONLINE_SERVERS, optServer.hashKey());
            redisTemplate.opsForList().remove(OFFLINE_SERVERS, 0, offLineServer);
            redisTemplate.opsForHash().delete(CLEANING_SERVERS, optServer.hashKey());
            log.info("offLine server {} successfuly.", offLineServer.toString());
          } catch (Exception e) {
            log.error("StatusSyncTimer error. ", e);
            return;
          }
        }


      //TODO 测试完后变回原有配置
      //}, initDelay, taskDelay, TimeUnit.SECONDS);

      }, 1, 10, TimeUnit.SECONDS);

    } catch (Exception e) {
      log.error("StatusSyncTimer stopped ..", e);
    }
  }

}





