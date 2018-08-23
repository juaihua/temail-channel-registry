package com.syswin.temail.cdtp.status.user.controller;

import com.syswin.temail.cdtp.status.user.beans.TemailAccountStatusLocateResponse;
import com.syswin.temail.cdtp.status.user.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.cdtp.status.user.beans.TemailAccountStatusUpdateResponse;
import com.syswin.temail.cdtp.status.user.service.ConnectionStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by juaihua on 2018/8/13.
 */
@RestController
public class ConnectionStatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionStatusController.class);

    @Autowired
    private ConnectionStatusService connectionStatusService;

    @PostMapping(value = "/updateStatus")
    public TemailAccountStatusUpdateResponse updateStatus(@RequestBody  TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest){
        return connectionStatusService.updateStatus(temailAccountStatusUpdateRequest);
    }


    @RequestMapping(value = "/locateStatus/{account}")
    public TemailAccountStatusLocateResponse locateStatus(@PathVariable String account){
        return connectionStatusService.locateStatus(account);
    }

}
