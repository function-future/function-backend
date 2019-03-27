package com.future.function.web.model.base;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataResponse<T> extends BaseResponse {

  private T data;

  public DataResponse() {}

  @Builder
  public DataResponse(int code, String status, T data) {

    super(code, status);
    this.data = data;
  }

}
