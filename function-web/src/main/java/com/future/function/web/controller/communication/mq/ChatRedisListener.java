package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.model.mq.ChatPayload;
import com.future.function.web.model.request.communication.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChatRedisListener implements BaseListener {

  private final ObjectMapper objectMapper;

  private final MessageService messageService;

  private final ChatroomService chatroomService;

  private final MessageStatusService messageStatusService;

  private final MessageRequestMapper messageRequestMapper;

  @Autowired
  public ChatRedisListener(ObjectMapper objectMapper, MessageService messageService, ChatroomService chatroomService, MessageStatusService messageStatusService, MessageRequestMapper messageRequestMapper) {
    this.objectMapper = objectMapper;
    this.messageService = messageService;
    this.chatroomService = chatroomService;
    this.messageStatusService = messageStatusService;
    this.messageRequestMapper = messageRequestMapper;
  }

  private void processMessage(MessageRequest messageRequest, String userId, String chatroomId) {
    if (chatroomId.equalsIgnoreCase("public")) {
      chatroomId = chatroomService.getPublicChatroom()
              .getId();
    }
    Message chatMessage = messageService.createMessage(
            messageRequestMapper.toMessage(messageRequest, userId, chatroomId), userId);
    Chatroom chatroom = chatroomService.getChatroom(
            chatroomId, userId);
    if (!chatroom.getType()
            .equals(ChatroomType.PUBLIC)) {
      chatroom.getMembers()
              .forEach(member -> messageStatusService.createMessageStatus(
                      MessageStatus.builder()
                              .message(chatMessage)
                              .chatroom(chatroom)
                              .member(member)
                              .seen(member.getId()
                                      .equals(userId))
                              .build(), userId));
    }
  }

  @Override
  public void onMessage(org.springframework.data.redis.connection.Message message, byte[] bytes) {
    ChatPayload chatPayload = null;
    try {
      chatPayload = objectMapper.readValue(message.getBody(), ChatPayload.class);
      processMessage(chatPayload.getMessageRequest(), chatPayload.getUserId(), chatPayload.getChatroomId());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
