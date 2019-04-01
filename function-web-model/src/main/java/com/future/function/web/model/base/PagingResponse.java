package com.future.function.web.model.base;

import com.future.function.web.model.base.paging.Paging;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Model representation for data response with paging.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PagingResponse<T> extends BaseResponse {
  
  private List<T> data;
  
  private Paging paging;
  
  public PagingResponse() {}
  
  @Builder
  private PagingResponse(int code, String status, List<T> data, Paging paging) {
    
    super(code, status);
    this.data = data;
    this.paging = paging;
  }
  
}
