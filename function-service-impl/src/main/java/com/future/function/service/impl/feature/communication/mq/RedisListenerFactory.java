package com.future.function.service.impl.feature.communication.mq;

import com.future.function.service.api.feature.communication.mq.MessageListenerFactory;
import com.future.function.service.api.feature.communication.mq.BaseListener;
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
