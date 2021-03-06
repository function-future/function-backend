package com.future.function.web.model.response.feature.communication.chatting;

import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomResponse {

  private String id;

  private String type;

  private String name;

  private FileContentWebResponse picture;

  private List<ChatroomParticipantResponse> participants;

  private LastMessageResponse lastMessage;

}
