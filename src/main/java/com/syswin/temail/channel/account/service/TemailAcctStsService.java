package com.syswin.temail.channel.account.service;


import static com.syswin.temail.channel.account.contants.RedisOptConstants.CLEANING_SERVERS;
import static com.syswin.temail.channel.account.contants.RedisOptConstants.HOST_PREFIX_ON_REDIS;
import static com.syswin.temail.channel.account.contants.RedisOptConstants.OFFLINE_SERVERS;
import static com.syswin.temail.channel.account.contants.RedisOptConstants.ONLINE_SERVERS;
import static com.syswin.temail.channel.account.contants.RedisOptConstants.TEMAIL_PREFIX_ON_REDIS;
import com.google.gson.Gson;
import com.syswin.temail.channel.account.beans.CdtpServer;
import com.syswin.temail.channel.account.beans.ComnRespData;
import com.syswin.temail.channel.account.beans.TemailAcctSts;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.contants.RedisOptConstants;
import com.syswin.temail.channel.exceptions.TemailDiscoveryException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemailAcctStsService {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  private static final Gson GSON = new Gson();


  /**
   * persistent channels into redis
   */
  public void addStatus(TemailAcctStses temailAcctStses) {
    try {
      temailAcctStses.getStatuses().forEach(status -> {
        String temailChannelHashKey = status.geneHashKey();
        String acctKey = new StringBuilder(TEMAIL_PREFIX_ON_REDIS).append(status.getAccount()).toString();
        redisTemplate.opsForHash().put(acctKey, temailChannelHashKey, status);

        String hstKey = new StringBuffer(HOST_PREFIX_ON_REDIS)
            .append(status.getHostOf()).append("-")
            .append(status.getProcessId()).append(":")
            .append(status.getAccount()).toString();
        redisTemplate.opsForHash().put(hstKey, temailChannelHashKey, status);
        log.debug("add Statuses service response: {}", GSON.toJson(temailAcctStses));
      });
    } catch (Exception e) {
      log.error("add status fail : {}", GSON.toJson(temailAcctStses));
      throw new TemailDiscoveryException("Failed to update gateway location with " + temailAcctStses, e);
    }
  }


  /**
   * delete channels into redis
   */
  public void delStatus(TemailAcctStses temailAcctStses) {
    try {
      temailAcctStses.getStatuses().stream().forEach(status -> {
        String acctKey = new StringBuilder(TEMAIL_PREFIX_ON_REDIS).append(status.getAccount()).toString();
        String temailChannelHashKey = status.geneHashKey();
        TemailAcctSts accts = (TemailAcctSts) redisTemplate.opsForHash().get(acctKey, temailChannelHashKey);
        if ((accts == null) || (accts.getHostOf().equals(status.getHostOf()) && accts.getProcessId()
            .equals(status.getProcessId()))) {
          redisTemplate.opsForHash().delete(acctKey, temailChannelHashKey);
          String hstKey = new StringBuffer(HOST_PREFIX_ON_REDIS)
              .append(status.getHostOf()).append("-")
              .append(status.getProcessId()).append(":")
              .append(status.getAccount()).toString();
          redisTemplate.opsForHash().delete(hstKey, temailChannelHashKey);
          log.debug("delete statuses : {} successfully ", GSON.toJson(temailAcctStses));
        } else {
          log.debug(
              "the server: {} which invoke this delete request is not the server:{} which is currently holding this channel resitry status, ignore this request ! ",
              GSON.toJson(status), GSON.toJson(accts), GSON.toJson(temailAcctStses));
        }
      });
    } catch (Exception e) {
      log.error("delete status fail : {}", GSON.toJson(temailAcctStses));
      throw new TemailDiscoveryException("failed to update gateway location with " + temailAcctStses, e);
    }
  }


  /**
   * obtain channels info
   */
  public TemailAcctStses locateStatus(String temailAccount) {
    Map<String, TemailAcctSts> tmpRes = new HashMap<String, TemailAcctSts>();
    TemailAcctStses result = new TemailAcctStses();
    try {
      Map<Object, Object> statusHash = redisTemplate.opsForHash().entries(TEMAIL_PREFIX_ON_REDIS + temailAccount);
      Optional.ofNullable(statusHash).ifPresent(new Consumer<Map>() {
        @Override
        public void accept(Map map) {
          map.forEach((k, v) -> {
            TemailAcctSts tmp = ((TemailAcctSts) v);
            tmpRes.put(tmp.dispathUniqueKey(), tmp);
          });
        }
      });

      result.setStatuses(new ArrayList(tmpRes.values()));
      log.debug("locate statuses account: {} , response: {}", temailAccount, GSON.toJson(result));
    } catch (Exception e) {
      log.error("locate status fail", temailAccount);
      throw new TemailDiscoveryException("failed to locate gateway location with " + temailAccount, e);
    }
    return result;
  }


  /**
   * allow a cdtp server to register or recorvery the state to onLine
   */
  public void registerOrRecorveryServer(CdtpServer cdtpServer) {
    if (Optional.ofNullable(redisTemplate.opsForHash().hasKey(CLEANING_SERVERS, cdtpServer.hashKey())).orElse(false)) {
      throw new IllegalStateException("the cdtpServer is cleaning now, no allowed to register");
    }
    // if parameter server instance alerady in offLine servers that means the cdtp-server reconnect in the ttl time
    // this condition may be caused by network error or one cdpt-status server error, so the cdtp-server try to reconnect!
    redisTemplate.opsForList().range(OFFLINE_SERVERS, 0, -1).stream()
        .filter(c1 -> ((CdtpServer) c1).getIp().equals(cdtpServer.getIp()) && ((CdtpServer) c1).getProcessId()
            .equals(cdtpServer.getProcessId()))
        .forEach(c2 -> {
          redisTemplate.opsForList().remove(OFFLINE_SERVERS, 0L, c2);
        });
    cdtpServer.setCdtpServerState(CdtpServer.CdtpServerState.onLine);
    cdtpServer.setCurStateBeginTime(RedisOptConstants.format(new Date()));
    redisTemplate.opsForHash().put(ONLINE_SERVERS, cdtpServer.hashKey(), cdtpServer);
    log.info("register successfully : {} ", cdtpServer.toString());
  }


  /**
   * offLine the server state by moving {@link CdtpServer} from Online-Servers to OffLine-Servers
   */
  public ComnRespData offLineTheServer(CdtpServer cdtpServer) {
    try {
      // we offLine a server based on the lateset request
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
      log.info("offLine the server success : {}", cdtpServer.toString());
      return new ComnRespData("offLine the server successfully...", true);
    } catch (Exception e) {
      log.error("offLine the server fail...", e);
      return new ComnRespData("offLine the server fail..." + e.getMessage(), false);
    }
  }


  /**
   * if server is in offLine state, reset to onLine
   *
   * @param cdtpServer
   */
  public void fixPotentialMiskakeOffLine(CdtpServer cdtpServer) {
    Object o = redisTemplate.opsForHash().get(ONLINE_SERVERS, cdtpServer.hashKey());
    if (o == null || ((CdtpServer) o).getCdtpServerState()
        .equals(CdtpServer.CdtpServerState.offLine)) {
      this.registerOrRecorveryServer(cdtpServer);
    }
  }
}
