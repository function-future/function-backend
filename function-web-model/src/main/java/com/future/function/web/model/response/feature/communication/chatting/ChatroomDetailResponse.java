package com.future.function.web.model.response.feature.communication.chatting;

import com.future.function.web.model.response.feature.embedded.ParticipantDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDetailResponse {

  private String id;

  private String name;

  private List<ParticipantDetailResponse> members;

  private String type;

}
