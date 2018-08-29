package com.syswin.temail.channel.account.controller;

import com.syswin.temail.channel.account.beans.ComnRespData;
import com.syswin.temail.channel.account.beans.Response;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class TemailAcctStsController {

  @Autowired
  private TemailAcctStsService connectionStatusService;

  @PostMapping(value = "/locations")
  @ResponseStatus(CREATED)
  public Response addStatus(
      @RequestBody TemailAcctStses temailAcctStses) {
    ComnRespData resp = connectionStatusService.addStatus(temailAcctStses);
    log.debug("add location: {}",resp.toString());
    return Response.ok(CREATED,resp);
  }

  @DeleteMapping(value = "/locations")
  @ResponseStatus(OK)
  public Response delStatus(
      @RequestBody TemailAcctStses temailAcctStses) {
    ComnRespData resp = connectionStatusService.delStatus(temailAcctStses);
    log.debug("remove location: {}",resp.toString());
    return Response.ok(OK,resp);
  }

  @GetMapping(value = "/locations/{account}")
  @ResponseStatus(OK)
  public Response locateStatus(@PathVariable String account) {
    TemailAcctStses resp = connectionStatusService.locateStatus(account);
    log.debug("locate location: {}",resp.toString());
    return Response.ok(OK,resp);
  }

}
