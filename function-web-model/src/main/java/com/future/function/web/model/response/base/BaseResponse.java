package com.future.function.web.model.response.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for base response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
  
  private int code;
  
  private String status;
  
}
