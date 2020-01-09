package com.future.function.service.impl.feature.communication.mq;

import com.future.function.service.api.feature.communication.mq.MessagePublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisherServiceImpl implements MessagePublisherService {

  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public RedisPublisherServiceImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public void publish(Object message, String topic) {
    redisTemplate.convertAndSend(topic, message);
  }
}
