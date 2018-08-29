package com.syswin.temail.channel.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Temail channel status bean Created by juaihua on 2018/8/14.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemailAcctSts {

  private String devId;
  private String hostOf;
  private String processId;
  private String mqTopic;
  private String mqTag;
}
