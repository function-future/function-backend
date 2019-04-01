package com.future.function.common.exception;

public class UnauthorizedException extends RuntimeException {
  
  public UnauthorizedException(String message) {
    
    this(message, null);
  }
  
  public UnauthorizedException(String message, Throwable cause) {
    
    super(message, cause);
  }
  
}
