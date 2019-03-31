package com.future.function.web.mapper.helper;

import com.future.function.web.model.base.BaseResponse;
import org.springframework.http.HttpStatus;

public class ResponseHelper {
  
  public static BaseResponse toBaseResponse(HttpStatus httpStatus) {
    
    return new BaseResponse(
      httpStatus.value(), toProperStatusFormat(httpStatus.getReasonPhrase()));
  }
  
  public static String toProperStatusFormat(String status) {
    
    return status.toUpperCase()
      .replace(" ", "_");
  }
  
}
