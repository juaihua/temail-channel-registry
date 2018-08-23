package com.syswin.temail.channel.account.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
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
public class TemailAccountStatusLocateResponse {

    private String account;

    private List<TemailAccountStatus> statusList ;

}
