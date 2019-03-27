package com.future.function.web.model.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWebResponse {

  private String role;

  private String email;

  private String name;

  private String phone;

  private String address;

  private String pictureUrl;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private long batch;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String university;

}
