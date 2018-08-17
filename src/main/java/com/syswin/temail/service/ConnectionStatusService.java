package com.syswin.temail.service;

import com.syswin.temail.beans.TemailAccountStatusLocateResponse;
import com.syswin.temail.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.beans.TemailAccountStatusUpdateResponse;

/**
 * channel service
 * Created by juaihua on 2018/8/15.
 */
public interface ConnectionStatusService {

    /**
     * persistent channels into redis
     * @param temailAccountStatusUpdateRequest
     * @return
     */
    public TemailAccountStatusUpdateResponse updateStatus(TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest);

    /**
     * obtain channels info
     */
    public TemailAccountStatusLocateResponse locateStatus(String temailAccount);

}
