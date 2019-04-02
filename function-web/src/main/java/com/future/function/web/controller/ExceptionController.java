package com.future.function.web.controller;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice class for exception handling purposes.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {
  
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestException.class)
  public ErrorResponse constraintViolationException(BadRequestException e) {
    
    log.error(e.getMessage(), e.getCause(), e.getConstraintViolations());
    
    return ResponseHelper.toErrorResponse(
      HttpStatus.BAD_REQUEST, e.getConstraintViolations());
  }
  
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UnauthorizedException.class)
  public BaseResponse unauthorizedException(UnauthorizedException e) {
    
    log.error(e.getMessage(), e.getCause());
    
    return ResponseHelper.toBaseResponse(HttpStatus.UNAUTHORIZED);
  }
  
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(ForbiddenException.class)
  public BaseResponse forbiddenException(ForbiddenException e) {
    
    log.error(e.getMessage(), e.getCause());
    
    return ResponseHelper.toBaseResponse(HttpStatus.FORBIDDEN);
  }
  
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public BaseResponse notFoundException(NotFoundException e) {
    
    log.error(e.getMessage(), e.getCause());
    
    return ResponseHelper.toBaseResponse(HttpStatus.NOT_FOUND);
  }
  
}
