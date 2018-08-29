package com.syswin.temail.channel.core.entity;

import lombok.AllArgsConstructor;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@AllArgsConstructor
public class TemailAcctStsUpdResp {

  private TemailAcctStsUpdRespResult result;

  private String msg;

  public TemailAcctStsUpdResp() {
  }

  public TemailAcctStsUpdResp(boolean isSuccess) {
    this.result =
        isSuccess ? TemailAcctStsUpdRespResult.success : TemailAcctStsUpdRespResult.fail;
    this.msg = isSuccess ? "" : "fail";
  }

  public enum TemailAcctStsUpdRespResult {
    success, fail
  }

}
