package com.future.function.web.model.response.feature.communication.chatting;

import com.future.function.common.enumeration.communication.ChatroomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 15:22 01/06/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomResponse {

  private String id;

  private String type;

  private String name;

  private List<ChatroomParticipantResponse> participants;

  private LastMessageResponse lastMessage;


}
