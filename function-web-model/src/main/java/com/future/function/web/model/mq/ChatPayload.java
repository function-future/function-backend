package com.future.function.web.model.mq;

import com.future.function.web.model.request.communication.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatPayload {
  private String chatroomId;
  private MessageRequest messageRequest;
  private String userId;
}
