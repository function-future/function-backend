package com.future.function.web.model.response.feature.core.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for author in web responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorWebResponse {
  
  private String id;
  
  private String name;
  
}
