package com.future.function.web.mapper.request.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public MessageRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public Message toMessage(
    MessageRequest request, String userId, String chatroomId
  ) {

    return toValidatedMessage(request, userId, chatroomId);
  }

  private Message toValidatedMessage(
    MessageRequest request, String userId, String chatroomId
  ) {

    validator.validate(request);

    return Message.builder()
      .chatroom(Chatroom.builder()
                  .id(chatroomId)
                  .build())
      .sender(User.builder()
                .id(userId)
                .build())
      .text(request.getMessage())
      .build();
  }

}
