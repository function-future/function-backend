package com.future.function.web.model.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
