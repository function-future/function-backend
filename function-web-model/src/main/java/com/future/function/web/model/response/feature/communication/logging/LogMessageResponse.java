package com.future.function.web.model.response.feature.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Ricky Kennedy
 * Created At : 23:09 27/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageResponse {

  private String id;

  private String text;

  private long createdAt;

  private String senderName;

  private String senderAvatar;
}
