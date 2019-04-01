package com.future.function.common.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class BadRequestException extends ConstraintViolationException {
  
  public BadRequestException(String message) {
    
    this(message, null);
  }
  
  public <T> BadRequestException(
    String message, Set<ConstraintViolation<T>> violations
  ) {
    
    super(message, violations);
  }
  
}
