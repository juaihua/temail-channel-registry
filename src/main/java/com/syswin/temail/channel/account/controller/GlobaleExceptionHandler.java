package com.syswin.temail.channel.account.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.syswin.temail.channel.account.beans.Response;
import com.syswin.temail.channel.exceptions.TemailDiscoveryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class GlobaleExceptionHandler {

  @ExceptionHandler(TemailDiscoveryException.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  Response<String> channelUpdateException(TemailDiscoveryException ex) {
    log.error("Failed to update channel status..", ex);
    return Response.failed(INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  Response<String> unexpectedException(Exception ex) {
    log.error("服务器请求异常", ex);
    return Response.failed(INTERNAL_SERVER_ERROR, ex.getMessage());
  }
}
