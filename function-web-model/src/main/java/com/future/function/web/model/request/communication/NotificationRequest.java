package com.future.function.web.model.request.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

  @NotNull(message = "Target user must not null")
  private String targetUser;

  @NotNull(message = "Title must not null")
  @Length(min = 1,
          max = 30,
          message = "Title length must between 1 and 30")
  private String title;

  @Length(max = 140,
          message = "Description length must less than equal 140")
  private String description;

}
