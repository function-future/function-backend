package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.UniqueBatchCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Model representation for batch web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchWebRequest {
  
  private String name;
  
  @UniqueBatchCode
  @NotNull(message = "NotNull")
  private Long code;
  
}
