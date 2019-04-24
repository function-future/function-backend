package com.future.function.web.dummy.controller;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class BadController {
  
  private static final String MESSAGE = "message";
  
  @GetMapping(value = "/bad-request")
  public String badRequest() {
    
    throw new BadRequestException(MESSAGE);
  }
  
  @GetMapping(value = "/bad-request-set")
  public String badRequestSet() {
    
    throw new BadRequestException(MESSAGE, Collections.emptySet());
  }
  
  @GetMapping(value = "/unauthorized")
  public String unauthorized() {
    
    throw new UnauthorizedException(MESSAGE);
  }
  
  @GetMapping(value = "/unauthorized-throwable")
  public String unauthorizedThrowable() {
    
    throw new UnauthorizedException(MESSAGE, new Throwable());
  }
  
  @GetMapping(value = "/forbidden")
  public String forbidden() {
    
    throw new ForbiddenException(MESSAGE);
  }
  
  @GetMapping(value = "/forbidden-throwable")
  public String forbiddenThrowable() {
    
    throw new ForbiddenException(MESSAGE, new Throwable());
  }
  
  @GetMapping(value = "/not-found")
  public String notFound() {
    
    throw new NotFoundException(MESSAGE);
  }
  
  @GetMapping(value = "/not-found-throwable")
  public String notFoundThrowable() {
    
    throw new NotFoundException(MESSAGE, new Throwable());
  }
  
}
