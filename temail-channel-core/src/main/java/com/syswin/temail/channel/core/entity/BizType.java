package com.syswin.temail.channel.core.entity;

import lombok.Getter;

/**
 * 请求类型
 *
 * @author 姚华成
 * @date 2018-8-21
 */
@Getter
public enum BizType {
  HEART_BEAT(0, HeartBeatRequest.class, HeartBeatResponse.class),
  SERVER_STATUS(1, ServerStatusRequest.class, ServerStatusResponse.class),
  USER_STATUS(2, TemailAcctStsUpdReq.class, TemailAcctStsUpdResp.class);

  private final int code;
  private Class<?> requestClass;
  private Class<?> responseClass;

  BizType(int code, Class<?> requestClass, Class<?> responseClass) {
    this.code = code;
    this.requestClass = requestClass;
    this.responseClass = responseClass;
  }

  public static BizType valueOf(int code) {
    switch (code) {
      case 0:
        return HEART_BEAT;
      case 1:
        return SERVER_STATUS;
      case 2:
        return USER_STATUS;
      default:
        throw new RuntimeException("不支持的业务类型！" + code);
    }
  }
}
