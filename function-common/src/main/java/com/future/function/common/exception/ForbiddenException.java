package com.future.function.common.exception;

/**
 * Exception class to be used when a forbidden resource/operation is
 * attempted to be retrieved/executed. Useful in authorization purposes.
 * <p>
 * This class extends {@link RuntimeException} as this exception is expected
 * to be thrown in runtime.
 */
public class ForbiddenException extends RuntimeException {

  public ForbiddenException(String message) {

    this(message, null);
  }

  public ForbiddenException(String message, Throwable cause) {

    super(message, cause);
  }

}
