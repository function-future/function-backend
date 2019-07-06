package com.future.function.web.model.request.communication;

import com.future.function.validation.annotation.communication.NotificationContent;
import com.future.function.validation.annotation.communication.NotificationTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @NotificationTitle
  private String title;

  @NotificationContent
  private String description;

}
