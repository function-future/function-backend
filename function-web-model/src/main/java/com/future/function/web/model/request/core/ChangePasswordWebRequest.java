package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordWebRequest {

  @NotBlank(message = "NotBlank")
  private String oldPassword;

  @NotBlank(message = "NotBlank")
  private String newPassword;

}
