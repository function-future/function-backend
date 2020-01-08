package com.future.function.service.api.feature.communication.mq;

public interface MessagePublisherService {
  void publish(Object message, String topic);
}
