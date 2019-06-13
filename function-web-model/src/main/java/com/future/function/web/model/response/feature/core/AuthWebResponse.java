package com.future.function.web.model.response.feature.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthWebResponse {
  
  private String name;
  
  private String role;
  
  private String email;
  
  private String avatar;
  
}
