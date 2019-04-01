package com.future.function.web.model.base;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Model representation for error response; specifically
 * {@link com.future.function.common.exception.BadRequestException}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends BaseResponse {
  
  private Map<String, List<String>> errors;
  
  public ErrorResponse() {}
  
  @Builder
  private ErrorResponse(
    int code, String status, Map<String, List<String>> errors
  ) {
    
    super(code, status);
    this.errors = errors;
  }
  
}
