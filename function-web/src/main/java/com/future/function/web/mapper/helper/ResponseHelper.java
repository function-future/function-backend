package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.ErrorResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHelper {
  
  public static BaseResponse toBaseResponse(HttpStatus httpStatus) {
    
    return new BaseResponse(
      httpStatus.value(), toProperStatusFormat(httpStatus.getReasonPhrase()));
  }
  
  public static String toProperStatusFormat(String status) {
    
    return status.toUpperCase()
      .replace(" ", "_");
  }
  
  public static ErrorResponse toErrorResponse(
    HttpStatus httpStatus, Set<ConstraintViolation<?>> violations
  ) {
    
    return ErrorResponse.builder()
      .code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .errors(ErrorHelper.toErrors(violations))
      .build();
  }
  
}
