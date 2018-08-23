package com.syswin.temail.cdtp.status.user.service;

import com.syswin.temail.cdtp.status.user.beans.TemailAccountStatusLocateResponse;
import com.syswin.temail.cdtp.status.user.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.cdtp.status.user.beans.TemailAccountStatusUpdateResponse;

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
