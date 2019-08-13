package com.future.function.common.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Set;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BadRequestException extends ConstraintViolationException {

  public BadRequestException(String message) {

    this(message, Collections.emptySet());
  }

  public <T> BadRequestException(
          String message, Set<ConstraintViolation<T>> violations
  ) {

    super(message, violations);
  }

}
