package com.future.function.common.exception;

import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Exception class to be used when a bad request object data is encountered.
 * <p>
 * This class extends {@link javax.validation.ConstraintViolationException} as
 * the parent class contains violations of the bad request object data, which
 * will be needed in either logging or web response.
 */
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
