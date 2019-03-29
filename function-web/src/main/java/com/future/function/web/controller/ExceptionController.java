package com.future.function.web.controller;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.future.function.common.validation.ErrorHelper;
import com.future.function.web.model.base.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorResponse constraintViolationException(ConstraintViolationException e) {

    return ErrorResponse.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .status(ErrorHelper.toProperStatusFormat(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .errors(ErrorHelper.toErrors(e.getConstraintViolations()))
        .build();
  }

}
