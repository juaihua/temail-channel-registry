package com.syswin.temail.channel.account.controller;

import com.google.gson.Gson;
import com.syswin.temail.channel.account.beans.Response;
import com.syswin.temail.channel.account.beans.TemailAcctStses;
import com.syswin.temail.channel.account.service.TemailAcctStsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class TemailAcctStsController {

  @Autowired
  private TemailAcctStsService connectionStatusService;

  private Gson gson = new Gson();

  @PostMapping(value = "/locations")
  @ResponseStatus(CREATED)
  public Response addStatus(
      @RequestBody TemailAcctStses temailAcctStses) {
    connectionStatusService.addStatus(temailAcctStses);
    log.debug("added location: {}", gson.toJson(temailAcctStses));
    return Response.ok(CREATED);
  }

  @PutMapping(value = "/locations")
  @ResponseStatus(OK)
  public Response delStatus(
      @RequestBody TemailAcctStses temailAcctStses) {
    connectionStatusService.delStatus(temailAcctStses);
    log.debug("remove location: {}", gson.toJson(temailAcctStses));
    return Response.ok();
  }

  @GetMapping(value = "/locations/{account}")
  @ResponseStatus(OK)
  public Response locateStatus(@PathVariable String account) {
    TemailAcctStses resp = connectionStatusService.locateStatus(account);
    log.debug("locate location: {}, {}", account, gson.toJson(resp));
    return Response.ok(OK, resp);
  }

}
