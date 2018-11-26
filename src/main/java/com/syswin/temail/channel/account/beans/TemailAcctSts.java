package com.syswin.temail.channel.account.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class TemailAcctSts {

  private String account;

  private String devId;

  private String hostOf;

  private String processId;

  private String mqTopic;

  private String mqTag;

  public String geneHashKey() {
    return this.devId;
    /*return new StringBuilder(this.devId).append("-").append(this.hostOf).append("-")
        .append(this.processId).append("-").append(mqTopic).append("-").append(mqTag).toString();*/
  }

  //
  public String dispathUniqueKey() {
    return new StringBuilder(this.account).append("|")
        .append(mqTopic).append("|").append(mqTag).toString();
  }

}
