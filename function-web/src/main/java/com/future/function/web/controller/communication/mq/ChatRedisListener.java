package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.mq.ChatPayload;
import com.future.function.web.model.request.communication.MessageRequest;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChatRedisListener implements BaseListener {

  private final ObjectMapper objectMapper;

  private final MessageService messageService;

  private final ChatroomService chatroomService;

  private final MessageStatusService messageStatusService;

  private final MessageRequestMapper messageRequestMapper;

  private final SimpMessagingTemplate messagingTemplate;

  private final FileProperties fileProperties;

  private final SetOperations<String, Object> redisSetOperations;

  @Autowired
  public ChatRedisListener(
          ObjectMapper objectMapper,
          MessageService messageService,
          ChatroomService chatroomService,
          MessageStatusService messageStatusService,
          MessageRequestMapper messageRequestMapper,
          SimpMessagingTemplate messagingTemplate,
          FileProperties fileProperties,
          RedisTemplate<String, Object> redisTemplate
  ) {
    this.objectMapper = objectMapper;
    this.messageService = messageService;
    this.chatroomService = chatroomService;
    this.messageStatusService = messageStatusService;
    this.messageRequestMapper = messageRequestMapper;
    this.messagingTemplate = messagingTemplate;
    this.fileProperties = fileProperties;
    this.redisSetOperations = redisTemplate.opsForSet();
  }

  private void processMessage(MessageRequest messageRequest, String userId, String chatroomId) {
    if (chatroomId.equalsIgnoreCase("public")) {
      chatroomId = chatroomService.getPublicChatroom()
              .getId();
    }
    Message chatMessage = messageService.createMessage(
            messageRequestMapper.toMessage(messageRequest, userId, chatroomId), userId);
    this.publishMessageToWebsocket(ChatroomResponseMapper.toMessageResponse(chatMessage, fileProperties.getUrlPrefix()), chatroomId);
    this.generateMessageStatus(chatroomId, userId, chatMessage);

  }

  private void publishMessageToWebsocket(MessageResponse message, String chatroomId) {
    if (chatroomService.getPublicChatroom().getId().equals(chatroomId)) {
      messagingTemplate.convertAndSend("/topic/chatrooms/public", message);
    } else {
      messagingTemplate.convertAndSend("/topic/chatrooms/" + chatroomId, message);
    }
  }

  private void generateMessageStatus(String chatroomId, String userId, Message chatMessage) {
    Chatroom chatroom = chatroomService.getChatroom(
            chatroomId, userId);
    if (!chatroom.getType().equals(ChatroomType.PUBLIC)) {
      chatroom.getMembers().forEach(member -> {
        messageStatusService.createMessageStatus(
                  MessageStatus.builder()
                          .message(chatMessage)
                          .chatroom(chatroom)
                          .member(member)
                          .seen(member.getId().equals(userId) || redisSetOperations
                                  .isMember("chatroom:" + chatroomId + ":active.user", member.getId()))
                          .build(), userId);
        }
      );
    }
  }

  @Override
  public void setContextHolder(String userId) {
    SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(userId, ""));
  }

  @Override
  public void onMessage(org.springframework.data.redis.connection.Message message, byte[] bytes) {
    ChatPayload chatPayload = null;
    try {
      chatPayload = objectMapper.readValue(message.getBody(), ChatPayload.class);
      this.setContextHolder(chatPayload.getUserId());
      this.processMessage(chatPayload.getMessageRequest(), chatPayload.getUserId(), chatPayload.getChatroomId());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
