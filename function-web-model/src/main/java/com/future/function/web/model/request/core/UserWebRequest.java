package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for user web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWebRequest {
  
  private String role;
  
  private String email;
  
  private String name;
  
  private String phone;
  
  private String address;
  
  private Long batch;
  
  private String university;
  
}
