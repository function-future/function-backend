package com.future.function.web.model.response.feature.communication.chatting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

  private String id;

  private ChatroomParticipantResponse sender;

  private String text;

  private Long time;

}
