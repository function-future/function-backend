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
public class UserWebResponse {

  private String id;

  private String role;

  private String email;

  private String name;

  private String phone;

  private String address;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String avatar;

  private String avatarId;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private BatchWebResponse batch;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String university;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private Integer finalPoint;

}
