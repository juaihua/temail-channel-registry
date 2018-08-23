package com.syswin.temail.channel.core.entity;

import lombok.Data;

/**
 * @author 姚华成
 * @date 2018-8-21
 */
@Data
public class ServerStatusRequestBody {

  private String ip;
  private String processId;

}
