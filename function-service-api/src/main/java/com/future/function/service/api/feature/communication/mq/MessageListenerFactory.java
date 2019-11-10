package com.future.function.service.api.feature.communication.mq;

public interface MessageListenerFactory {
  BaseListener getMessageListener(String topic);
}
