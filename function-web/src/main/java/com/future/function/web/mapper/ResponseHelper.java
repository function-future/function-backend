package com.future.function.web.mapper;

import org.springframework.http.HttpStatus;

import com.future.function.web.model.base.BaseResponse;

public class ResponseHelper {

  public static BaseResponse getBaseResponse() {

    return new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
  }

}
