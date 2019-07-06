package com.future.function.web.model.request.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Author: PriagungSatyagama
 * Created At: 19:07 06/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

  @NotNull
  private String targetUser;

  @NotNull
  @Length(min = 1, max = 30)
  private String title;

  @Length(max = 140)
  private String description;

}
