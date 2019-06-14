package com.future.function.session.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.session.model.Session;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JsonSerializer implements RedisSerializer<Session> {
  
  private final ObjectMapper objectMapper;
  
  public JsonSerializer(ObjectMapper objectMapper) {
    
    this.objectMapper = objectMapper;
  }
  
  @Override
  public byte[] serialize(Session session) {
    
    try {
      return objectMapper.writeValueAsBytes(session);
    } catch (Exception e) {
      throw new SerializationException(e.getMessage(), e);
    }
  }
  
  @Override
  public Session deserialize(byte[] bytes) {
    
    if (bytes == null) {
      return null;
    }
    
    try {
      return objectMapper.readValue(bytes, Session.class);
    } catch (Exception e) {
      throw new SerializationException(e.getMessage(), e);
    }
  }
  
}
