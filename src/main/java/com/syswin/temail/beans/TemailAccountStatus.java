package com.syswin.temail.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

/**
 * Temail channel status bean
 * Created by juaihua on 2018/8/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemailAccountStatus {

    private String devId;

    private String hostOf;

    private String mqTopic;

    private String processId;

    public TemailAccountStatus() {}

    public TemailAccountStatus(String devId, String hostOf, String mqTopic, String processId) {
        this.devId = devId;
        this.hostOf = hostOf;
        this.mqTopic = mqTopic;
        this.processId = processId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getHostOf() {
        return hostOf;
    }

    public void setHostOf(String hostOf) {
        this.hostOf = hostOf;
    }

    public String getMqTopic() {
        return mqTopic;
    }

    public void setMqTopic(String mqTopic) {
        this.mqTopic = mqTopic;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String geneHashKey(){
        return  new StringBuilder(this.devId).append("-").append(this.hostOf).append("-").append("mqTopic")
                    .append("-").append(Optional.ofNullable(this.processId).orElse("")).toString();
    }

    @Override
    public String toString() {
        return "TemailAccountStatus{" +
                "devId='" + devId + '\'' +
                ", hostOf='" + hostOf + '\'' +
                ", mqTopic='" + mqTopic + '\'' +
                '}';
    }
}
