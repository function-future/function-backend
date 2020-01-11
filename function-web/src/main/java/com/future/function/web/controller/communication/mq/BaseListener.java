package com.future.function.web.controller.communication.mq;

import org.springframework.data.redis.connection.MessageListener;

public interface BaseListener extends MessageListener {
  void setContextHolder(String userId);
}
