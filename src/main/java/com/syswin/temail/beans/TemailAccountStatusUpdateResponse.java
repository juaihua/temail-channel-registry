package com.syswin.temail.beans;

/**
 * temail channel update result
 * Created by juaihua on 2018/8/14.
 */
public class TemailAccountStatusUpdateResponse {

    private TemailAccountStatusUpdateResponseResult result;

    private String msg;

    public TemailAccountStatusUpdateResponse(boolean isSuccess) {
        this.result = isSuccess ? TemailAccountStatusUpdateResponseResult.success : TemailAccountStatusUpdateResponseResult.fail;
        this.msg = isSuccess ? "" : "fail";
    }

    public TemailAccountStatusUpdateResponse(TemailAccountStatusUpdateResponseResult result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public TemailAccountStatusUpdateResponse() {
    }

    public TemailAccountStatusUpdateResponseResult getResult() {
        return result;
    }

    public void setResult(TemailAccountStatusUpdateResponseResult result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static enum TemailAccountStatusUpdateResponseResult{
        success,fail;
    }


}
