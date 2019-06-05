package com.future.function.web.model.response.feature.core.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionWebResponse {
  
  private Long timestamp;
  
  private String url;
  
}
