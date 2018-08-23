package com.syswin.temail.cdtp.status.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemailAccountStatusUpdateRequest {

  private String account;

  private OptType optype;

  private TemailAccountStatus status;

  public enum OptType {
    add, del
  }
}
