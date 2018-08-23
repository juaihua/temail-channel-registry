package com.syswin.temail.cdtp.status.user.beans;

/**
 * temail channel update request
 * Created by juaihua on 2018/8/14.
 */
public class TemailAccountStatusUpdateRequest {

    private String account;

    private OptType optype;

    private TemailAccountStatus status;

    public TemailAccountStatusUpdateRequest() {}

    public TemailAccountStatusUpdateRequest(String account, OptType optype, TemailAccountStatus status) {
        this.account = account;
        this.optype = optype;
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public OptType getOptype() {
        return optype;
    }

    public void setOptype(OptType optype) {
        this.optype = optype;
    }

    public TemailAccountStatus getStatus() {
        return status;
    }

    public void setStatus(TemailAccountStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TemailAccountStatusUpdateRequest{" +
                "account='" + account + '\'' +
                ", optype=" + optype +
                ", status=" + status +
                '}';
    }

    public static enum OptType{
        add,del;
    }
}
