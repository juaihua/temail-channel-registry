package com.syswin.temail.channel.account.service;


import com.syswin.temail.channel.account.beans.*;
import com.syswin.temail.channel.account.contants.RedisOptConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

import static com.syswin.temail.channel.account.contants.RedisOptConstants.*;

/**
 * Created by juaihua on 2018/8/20.
 */
@Slf4j
@Component
public class ConnectionStatusServiceImpl {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  /**
   * persistent channels into redis
   */
  public TemailAccountStatusUpdateResponse updateStatus(
      TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest) {
    try {
      String temailChannelHashKey = temailAccountStatusUpdateRequest.getStatus().geneHashKey();
      switch (temailAccountStatusUpdateRequest.getOptype()) {
        case add: {
          String acctKey = new StringBuilder(TEMAIL_PREFIX_ON_REDIS)
              .append(temailAccountStatusUpdateRequest.getAccount()).toString();
          redisTemplate.opsForHash().put(acctKey, temailChannelHashKey, temailAccountStatusUpdateRequest.getStatus());
          String hstKey = new StringBuffer(HOST_PREFIX_ON_REDIS)
              .append(temailAccountStatusUpdateRequest.getStatus().getHostOf()).append("-")
              .append(temailAccountStatusUpdateRequest.getStatus().getProcessId()).append(":")
              .append(temailAccountStatusUpdateRequest.getAccount()).toString();
          redisTemplate.opsForHash().put(hstKey, temailChannelHashKey, temailAccountStatusUpdateRequest.getStatus());
          break;
        }
        case del: {
          String acctKey = new StringBuilder(TEMAIL_PREFIX_ON_REDIS)
              .append(temailAccountStatusUpdateRequest.getAccount()).toString();
          redisTemplate.opsForHash().delete(acctKey, temailChannelHashKey);
          String hstKey = new StringBuffer(HOST_PREFIX_ON_REDIS)
              .append(temailAccountStatusUpdateRequest.getStatus().getHostOf()).append("-")
              .append(temailAccountStatusUpdateRequest.getStatus().getProcessId()).append(":")
              .append(temailAccountStatusUpdateRequest.getAccount()).toString();
          redisTemplate.opsForHash().delete(hstKey, temailChannelHashKey);
          break;
        }
      }
      return new TemailAccountStatusUpdateResponse(true);
    } catch (Exception e) {
      log.error("error in update channel status..", e);
      return new TemailAccountStatusUpdateResponse(false);
    }
  }


  /**
   * obtain channels info
   */
  public TemailAccountStatusLocateResponse locateStatus(String temailAccount) {
    TemailAccountStatusLocateResponse result = new TemailAccountStatusLocateResponse();
    result.setAccount(temailAccount);
    try {
      List<TemailAccountStatus> statusList = new ArrayList<TemailAccountStatus>();
      Map<Object, Object> statusHash = redisTemplate.opsForHash().entries(TEMAIL_PREFIX_ON_REDIS + temailAccount);
      Optional.ofNullable(statusHash).ifPresent(new Consumer<Map>() {
        @Override
        public void accept(Map map) {
          map.forEach((k, v) -> statusList.add((TemailAccountStatus) v));
        }
      });
      result.setStatusList(statusList);
    } catch (Exception e) {
      log.error("redis locate fail.", e);
      e.printStackTrace();
    }
    return result;
  }


  /**
   * allow a cdtp server to register or recorvery the state to onLine
   */
  public CdtpServerOperateResponse registerOrRecorveryServer(CdtpServer cdtpServer) {
    try {
      if (Optional.ofNullable(redisTemplate.opsForHash().hasKey(CLEANING_SERVERS, cdtpServer.hashKey()))
          .orElse(false)) {
        return new CdtpServerOperateResponse("the cdtpServer is cleaning now, no allowed to register", false);
      }
      redisTemplate.opsForList().range(OFFLINE_SERVERS, 0, -1).stream()
          .filter(c1 -> ((CdtpServer) c1).getIp().equals(cdtpServer.getIp()) && ((CdtpServer) c1).getProcessId()
              .equals(cdtpServer.getProcessId()))
          .forEach(c2 -> redisTemplate.opsForList().remove(OFFLINE_SERVERS, 0L, c2));
      cdtpServer.setCdtpServerState(CdtpServer.CdtpServerState.onLine);
      cdtpServer.setCurStateBeginTime(RedisOptConstants.format(new Date()));
      redisTemplate.opsForHash().put(ONLINE_SERVERS, cdtpServer.hashKey(), cdtpServer);
      log.debug("register successfully : {} ",cdtpServer.toString());
      return new CdtpServerOperateResponse("register successfully.. ", true);
    } catch (Exception e) {
      log.error("register fail... ",e.getMessage());
      return new CdtpServerOperateResponse("redis operate fail..", false);
    }
  }


  /**
   * offLine the server state by moving {@link CdtpServer} from Online-Servers to OffLine-Servers
   */
  public CdtpServerOperateResponse offLineTheServer(CdtpServer cdtpServer) {
    try {
      (redisTemplate.opsForList().range(OFFLINE_SERVERS, 0, -1)).stream()
          .filter(c1 -> ((CdtpServer) c1).getIp().equals(cdtpServer.getIp()) && ((CdtpServer) c1).getProcessId()
              .equals(cdtpServer.getProcessId()))
          .forEach(c2 -> {
            redisTemplate.opsForList().remove(OFFLINE_SERVERS, 0L, c2);
          });
      cdtpServer.setCdtpServerState(CdtpServer.CdtpServerState.offLine);
      cdtpServer.setCurStateBeginTime(RedisOptConstants.format(new Date()));
      redisTemplate.opsForList().rightPush(OFFLINE_SERVERS, cdtpServer);
      redisTemplate.opsForHash().put(ONLINE_SERVERS, cdtpServer.hashKey(), cdtpServer);
      log.debug("offLine the server success : {}", cdtpServer.toString());
      return new CdtpServerOperateResponse("offLine the server successfully...", true);
    } catch (Exception e) {
      log.error("offLine the server fail...", e);
      return new CdtpServerOperateResponse("offLine the server fail..." + e.getMessage(), false);
    }
  }

}
