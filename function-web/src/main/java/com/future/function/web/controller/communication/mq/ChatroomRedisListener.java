package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.properties.communication.RedisProperties;
import com.future.function.common.properties.communication.WsProperties;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;

@Component
public class ChatroomRedisListener implements BaseListener {

  private final ObjectMapper objectMapper;

  private final RedisProperties redisProperties;

  private final SimpMessagingTemplate messagingTemplate;

  private final ChatroomService chatroomService;

  private final MessageService messageService;

  private final MessageStatusService messageStatusService;

  private final UserService userService;

  private final ResourceService resourceService;

  private final FileProperties fileProperties;

  private final WsProperties wsProperties;

  private final ValueOperations<String, Object> valueOperations;

  @Autowired
  public ChatroomRedisListener(ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate,
                               RedisProperties redisProperties, SimpMessagingTemplate messagingTemplate,
                               ChatroomService chatroomService, MessageService messageService,
                               MessageStatusService messageStatusService, UserService userService,
                               ResourceService resourceService, FileProperties fileProperties, WsProperties wsProperties) {
    this.objectMapper = objectMapper;
    this.valueOperations = redisTemplate.opsForValue();
    this.redisProperties = redisProperties;
    this.messagingTemplate = messagingTemplate;
    this.chatroomService = chatroomService;
    this.messageService = messageService;
    this.messageStatusService = messageStatusService;
    this.userService = userService;
    this.resourceService = resourceService;
    this.fileProperties = fileProperties;
    this.wsProperties = wsProperties;
  }

  @Override
  public void setContextHolder(String userId) {
  }

  @Override
  public void onMessage(Message message, byte[] bytes) {
    try {
      String userId = objectMapper.readValue(message.getBody(), String.class);
      UriTemplate uriTemplate = new UriTemplate(redisProperties.getKey().get("limit-chatroom"));
      Long limit = objectMapper.convertValue(valueOperations.get(uriTemplate.expand(userId).toString()), Long.class);
      if (limit == null) {
        limit = 1L;
      }
      this.processMessage(userId, limit);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void processMessage(String userId, Long limit) {
    PagingResponse<ChatroomResponse> response = ChatroomResponseMapper.toPagingChatroomResponse(
      chatroomService.getChatrooms(userId, PageHelper.toPageable(1, limit.intValue())),
      messageService, messageStatusService, userService, resourceService, fileProperties.getUrlPrefix(), userId
    );
    this.publishMessageToWebsocket(response, userId);
  }

  private void publishMessageToWebsocket(PagingResponse<ChatroomResponse> message, String userId) {
    UriTemplate uriTemplate = new UriTemplate(wsProperties.getTopic().get("chatroom"));
    messagingTemplate.convertAndSend(uriTemplate.expand(userId).toString(), message);
  }
}
