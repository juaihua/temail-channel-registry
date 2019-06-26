/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.channel.account.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
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
    log.info("added location: {}", gson.toJson(temailAcctStses));
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
    log.info("locate location: {}, {}", account, gson.toJson(resp));
    return Response.ok(OK, resp);
  }

}
