package com.future.function.web.model.response.feature.core;

import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.paging.Paging;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataPageResponse<T> extends BaseResponse {
  
  private T data;
  
  private Paging paging;
  
  public DataPageResponse() {}
  
  @Builder
  private DataPageResponse(int code, String status, T data, Paging paging) {
    
    super(code, status);
    this.data = data;
    this.paging = paging;
  }
  
}
