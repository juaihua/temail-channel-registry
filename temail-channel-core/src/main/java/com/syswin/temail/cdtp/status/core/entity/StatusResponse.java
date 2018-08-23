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
public class StatusResponse<T> {

  private BizType bizType;
  private T responseBody;
}
