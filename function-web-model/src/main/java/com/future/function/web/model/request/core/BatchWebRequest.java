package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for batch web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchWebRequest {
  
  private String name;
  
  private Long code;
  
}
