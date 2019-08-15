package com.future.function.web.mapper.request.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.ChatroomRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ChatroomRequestMapper {

  private final RequestValidator validator;

  @Autowired
  private ChatroomRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public Chatroom toChatroom(ChatroomRequest request, String chatroomId) {

    return toValidatedChatroom(request, chatroomId);
  }

  private Chatroom toValidatedChatroom(
    ChatroomRequest request, String chatroomId
  ) {

    validator.validate(request);

    List<User> members = new ArrayList<>();
    request.getMembers()
      .forEach(userId -> {
        User member = User.builder()
          .id(userId)
          .build();
        members.add(member);
      });

    Chatroom chatroom = Chatroom.builder()
      .title(request.getName())
      .type(request.getMembers()
              .size() > 2 ? ChatroomType.GROUP : ChatroomType.PRIVATE)
      .members(members)
      .build();

    if (chatroomId != null) {
      chatroom.setId(chatroomId);
    }
    return chatroom;
  }

}
