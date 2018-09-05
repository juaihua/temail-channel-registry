package com.syswin.temail.channel.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Data
@AllArgsConstructor
public class TemailAccountStatusResp {

  private TemailAcctStsUpdRespResult result;

  private String msg;

  public TemailAccountStatusResp() {
  }

  public TemailAccountStatusResp(boolean isSuccess) {
    this.result =
        isSuccess ? TemailAcctStsUpdRespResult.success : TemailAcctStsUpdRespResult.fail;
    this.msg = isSuccess ? "" : "fail";
  }

  public enum TemailAcctStsUpdRespResult {
    success, fail
  }

}
