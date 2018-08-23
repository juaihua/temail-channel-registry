package com.syswin.temail.channel.account.contants;


import com.syswin.temail.channel.account.timer.StatusSyncTimer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by juaihua on 2018/8/20.
 */
public class RedisOptConstants {

  public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static final String REDIS_DATA_PREFIX = "temail:";
  public static final String TEMAIL_PREFIX_ON_REDIS = REDIS_DATA_PREFIX + "status:";
  public static final String HOST_PREFIX_ON_REDIS = REDIS_DATA_PREFIX + "hosts:";
  public static final String ONLINE_SERVERS = REDIS_DATA_PREFIX + "onLine-servers";
  public static final String OFFLINE_SERVERS = REDIS_DATA_PREFIX + "offLine-servers";
  public static final String CLEANING_SERVERS = REDIS_DATA_PREFIX + "cleaning-servers";
  private static final Logger LOGGER = LoggerFactory.getLogger(StatusSyncTimer.class);

  public static String format() {
    return format(new Date());
  }

  public static String format(Date date) {
    return YYYY_MM_DD_HH_MM_SS.format(date);
  }

  public static Date parse(String dtStr) throws ParseException {
    return YYYY_MM_DD_HH_MM_SS.parse(dtStr);
  }

  public static long calicTimeCap(String date) throws ParseException {
    return System.currentTimeMillis() - parse(date).getTime();
  }
}
