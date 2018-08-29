package com.syswin.temail.channel.account.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CdtpServer {

  private String ip;

  private String processId;

  //time cdtpserver instance offLine
  private String curStateBeginTime;

  private CdtpServerState cdtpServerState;

  public String hashKey() {
    return ip + "-" + processId;
  }

  //cdtpserver states..
  public static enum CdtpServerState {

    onLine(1, "onLine", "the cdtpserver instance is onLine now"),

    offLine(2, "offLine", "the cdtpserver instance is offLine now"),

    cleaning(3, "cleaning", "the cdtpserver instance channel status is cleaning now "),

    history(4, "history", "the cdtpserver instance has benn closed ");

    private int index;

    private String stateName;

    private String description;

    private CdtpServerState(int index, String stateName, String description) {
      this.index = index;
      this.stateName = stateName;
      this.description = description;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public String getStateName() {
      return stateName;
    }

    public void setStateName(String stateName) {
      this.stateName = stateName;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

}
