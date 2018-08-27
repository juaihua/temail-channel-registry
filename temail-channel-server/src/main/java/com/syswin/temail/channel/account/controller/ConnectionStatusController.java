package com.syswin.temail.channel.account.controller;

import com.syswin.temail.channel.account.beans.TemailAccountStatusLocateResponse;
import com.syswin.temail.channel.account.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.channel.account.beans.TemailAccountStatusUpdateResponse;
import com.syswin.temail.channel.account.service.ConnectionStatusServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by juaihua on 2018/8/13.
 */
@RestController
public class ConnectionStatusController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionStatusController.class);

  @Autowired
  private ConnectionStatusServiceImpl connectionStatusService;

  @RequestMapping(value = "/healthcheck ")
  public TemailAccountStatusLocateResponse locateStatus() {
    return ;
  }

  @PostMapping(value = "/updateStatus")
  public TemailAccountStatusUpdateResponse updateStatus(
      @RequestBody TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest) {
    return connectionStatusService.updateStatus(temailAccountStatusUpdateRequest);
  }


  @RequestMapping(value = "/locateStatus/{account}")
  public TemailAccountStatusLocateResponse locateStatus(@PathVariable String account) {
    return connectionStatusService.locateStatus(account);
  }

}
