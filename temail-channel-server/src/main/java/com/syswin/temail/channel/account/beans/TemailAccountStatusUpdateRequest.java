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
public class TemailAccountStatusUpdateRequest {

    private String account;

    private OptType optype;

    private TemailAccountStatus status;

    public static enum OptType{
        add,del;
    }
}
