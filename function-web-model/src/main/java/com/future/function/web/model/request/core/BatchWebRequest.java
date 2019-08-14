package com.future.function.web.model.request.core;

import com.future.function.common.data.core.BatchData;
import com.future.function.validation.annotation.core.Alphanumeric;
import com.future.function.validation.annotation.core.NoSpace;
import com.future.function.validation.annotation.core.UniqueBatchCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueBatchCode
public class BatchWebRequest implements BatchData {
  
  private String id;
  
  @NotBlank(message = "NotBlank")
  private String name;
  
  @NoSpace
  @Alphanumeric
  @NotBlank(message = "NotBlank")
  private String code;
  
}
