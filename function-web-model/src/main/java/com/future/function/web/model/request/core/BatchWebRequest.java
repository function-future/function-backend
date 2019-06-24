package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.NoSpace;
import com.future.function.validation.annotation.core.UniqueBatchCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Model representation for batch web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchWebRequest {
  
  private String name;
  
  @NoSpace
  @UniqueBatchCode
  @NotBlank(message = "NotBlank")
  private String code;
  
}
