package com.syswin.temail.cdtp.status.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@AllArgsConstructor
public class TemailAccountStatusUpdateResponse {

  private TemailAccountStatusUpdateResponseResult result;

  private String msg;

  public TemailAccountStatusUpdateResponse() {
  }

  public TemailAccountStatusUpdateResponse(boolean isSuccess) {
    this.result =
        isSuccess ? TemailAccountStatusUpdateResponseResult.success : TemailAccountStatusUpdateResponseResult.fail;
    this.msg = isSuccess ? "" : "fail";
  }

  public enum TemailAccountStatusUpdateResponseResult {
    success, fail
  }

}
