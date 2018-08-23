package com.syswin.temail.controller;

import com.syswin.temail.beans.TemailAccountStatusLocateResponse;
import com.syswin.temail.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.beans.TemailAccountStatusUpdateResponse;
import com.syswin.temail.service.ConnectionStatusServiceImpl;
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
    private ConnectionStatusServiceImpl connectionStatusService;

    @PostMapping(value = "/updateStatus")
    public TemailAccountStatusUpdateResponse updateStatus(@RequestBody  TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest){
        return connectionStatusService.updateStatus(temailAccountStatusUpdateRequest);
    }


    @RequestMapping(value = "/locateStatus/{account}")
    public TemailAccountStatusLocateResponse locateStatus(@PathVariable String account){
        return connectionStatusService.locateStatus(account);
    }

}
