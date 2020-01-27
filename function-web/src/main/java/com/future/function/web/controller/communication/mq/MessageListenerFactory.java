package com.future.function.web.controller.communication.mq;

public interface MessageListenerFactory {
  BaseListener getMessageListener(String topic);
}
