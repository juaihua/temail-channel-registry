package com.syswin.temail.channel.account.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by juaihua on 2018/8/14.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemailAccountStatus {

  private String devId;

  private String hostOf;

  private String processId;

  private String mqTopic;

  private String mqTag;

  public String geneHashKey() {
    return new StringBuilder(this.devId).append("-").append(this.hostOf).append("-")
        .append(this.processId).append("-").append(mqTopic).append("-").append(mqTag).toString();
  }

}
