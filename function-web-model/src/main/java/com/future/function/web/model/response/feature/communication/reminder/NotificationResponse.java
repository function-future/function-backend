package com.future.function.web.model.response.feature.communication.reminder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: PriagungSatyagama
 * Created At: 18:25 06/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

  private String id;

  private String targetUser;

  private String title;

  private String description;

  private Long createdAt;

}
