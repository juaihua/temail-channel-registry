package com.syswin.temail.beans;

import java.util.List;

/**
 * temail channel info locate response
 * Created by juaihua on 2018/8/14.
 */
public class TemailAccountStatusLocateResponse {

    private String account;

    private List<TemailAccountStatus> statusList ;

    public TemailAccountStatusLocateResponse() {}

    public TemailAccountStatusLocateResponse(String account, List<TemailAccountStatus> statusList) {
        this.account = account;
        this.statusList = statusList;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<TemailAccountStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<TemailAccountStatus> statusList) {
        this.statusList = statusList;
    }

    @Override
    public String toString() {
        return "TemailAccountStatusLocateResponse{" +
                "account='" + account + '\'' +
                ", statusList=" + statusList +
                '}';
    }
}
