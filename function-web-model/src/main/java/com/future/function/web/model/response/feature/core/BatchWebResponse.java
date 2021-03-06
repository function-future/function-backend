package com.future.function.web.model.response.feature.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchWebResponse {

  private String id;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String name;

  private String code;

}
