package com.syswin.temail.channel.account.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by juaihua on 2018/8/14.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemailAccountStatusUpdateResponse {

    private String msg;

    private TemailAccountStatusUpdateResponseResult result;

    public TemailAccountStatusUpdateResponse(boolean isSuccess) {
        this.result = isSuccess ? TemailAccountStatusUpdateResponseResult.success : TemailAccountStatusUpdateResponseResult.fail;
        this.msg = isSuccess ? "" : "fail";
    }

    public TemailAccountStatusUpdateResponse(TemailAccountStatusUpdateResponseResult result, String msg) {
        this.result = result;
        this.msg = msg;
    }


    public static enum TemailAccountStatusUpdateResponseResult{
        success,fail;
    }

}
