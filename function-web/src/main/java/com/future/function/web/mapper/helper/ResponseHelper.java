package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.BaseResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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
  
}
