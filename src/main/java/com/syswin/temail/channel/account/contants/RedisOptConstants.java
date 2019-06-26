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

package com.syswin.temail.channel.account.contants;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.syswin.temail.channel.account.timer.StatusSyncTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisOptConstants {

  public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static final String REDIS_DATA_PREFIX = "temail:";
  public static final String TEMAIL_PREFIX_ON_REDIS = REDIS_DATA_PREFIX + "status:";
  public static final String HOST_PREFIX_ON_REDIS = REDIS_DATA_PREFIX + "hosts:";
  public static final String ONLINE_SERVERS = REDIS_DATA_PREFIX + "onLine-servers";
  public static final String OFFLINE_SERVERS = REDIS_DATA_PREFIX + "offLine-servers";
  public static final String CLEANING_SERVERS = REDIS_DATA_PREFIX + "cleaning-servers";

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
