package com.future.function.web.model.response.feature.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for user web response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWebResponse {
  
  private String id;
  
  private String role;
  
  private String email;
  
  private String name;
  
  private String phone;
  
  private String address;
  
  private boolean deleted;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String avatar;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String batch;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String university;
  
}
