package com.future.function.service.impl.feature.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.repository.feature.communication.chatting.MessageRepository;
import com.future.function.service.api.feature.communication.mq.BaseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChatRedisListener implements BaseListener {

  private final ObjectMapper objectMapper;

  private final MessageRepository messageRepository;

  @Autowired
  public ChatRedisListener(ObjectMapper objectMapper, MessageRepository messageRepository) {
    this.objectMapper = objectMapper;
    this.messageRepository = messageRepository;
  }

  @Override
  public void onMessage(org.springframework.data.redis.connection.Message message, byte[] bytes) {
    try {
      Message chat = objectMapper.readValue(message.getBody(), Message.class);
      messageRepository.save(chat);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
