package com.future.function.web.controller.communication.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisListenerFactory implements MessageListenerFactory {

  private final ChatRedisListener chatRedisListener;

  private final NotificationRedisListener notificationRedisListener;

  private final ChatroomRedisListener chatroomRedisListener;

  @Autowired
  public RedisListenerFactory(ChatRedisListener chatRedisListener, NotificationRedisListener notificationRedisListener, ChatroomRedisListener chatroomRedisListener) {
    this.chatRedisListener = chatRedisListener;
    this.notificationRedisListener = notificationRedisListener;
    this.chatroomRedisListener = chatroomRedisListener;
  }

  public BaseListener getMessageListener(String topic) {
    switch (topic.toLowerCase()) {
      case "chat": return chatRedisListener;
      case "notification": return notificationRedisListener;
      case "chatroom": return chatroomRedisListener;
      default: return null;
    }
  }

}
