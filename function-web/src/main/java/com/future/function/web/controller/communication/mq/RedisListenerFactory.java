package com.future.function.web.controller.communication.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisListenerFactory implements MessageListenerFactory {

  private final ChatRedisListener chatRedisListener;

  @Autowired
  public RedisListenerFactory(ChatRedisListener chatRedisListener) {
    this.chatRedisListener = chatRedisListener;
  }

  public BaseListener getMessageListener(String topic) {
    switch (topic.toLowerCase()) {
      case "chat": return chatRedisListener;
      default: return null;
    }
  }

}
