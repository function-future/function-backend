package com.future.function.web.controller;

import com.future.function.web.mapper.helper.ErrorHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * Controller advice class for exception handling purposes.
 */
@RestControllerAdvice
public class ExceptionController {
  
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorResponse constraintViolationException(
    ConstraintViolationException e
  ) {
    
    return ErrorResponse.builder()
      .code(HttpStatus.BAD_REQUEST.value())
      .status(ResponseHelper.toProperStatusFormat(
        HttpStatus.BAD_REQUEST.getReasonPhrase()))
      .errors(ErrorHelper.toErrors(e.getConstraintViolations()))
      .build();
  }
  
}
