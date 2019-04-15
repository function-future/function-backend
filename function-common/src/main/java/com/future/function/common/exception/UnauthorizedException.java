package com.future.function.common.exception;

/**
 * Exception class to be used when a unauthorized resource/operation is
 * attempted to be retrieved/executed. Useful in authentication purposes.
 * <p>
 * This class extends {@link RuntimeException} as this exception is expected
 * to be thrown in runtime.
 */
public class UnauthorizedException extends RuntimeException {
  
  public UnauthorizedException(String message) {
    
    this(message, null);
  }
  
  public UnauthorizedException(String message, Throwable cause) {
    
    super(message, cause);
  }
  
}
