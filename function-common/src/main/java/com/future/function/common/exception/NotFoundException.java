package com.future.function.common.exception;

/**
 * Exception class to be used when a forbidden resource is not available/found.
 * <p>
 * This class extends {@link RuntimeException} as this exception is expected
 * to be thrown in runtime.
 */
public class NotFoundException extends RuntimeException {
  
  public NotFoundException(String message) {
    
    this(message, null);
  }
  
  public NotFoundException(String message, Throwable cause) {
    
    super(message, cause);
  }
  
}
