package com.future.function.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.session.model.Session;
import com.future.function.session.serializer.JsonSerializer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
  
  @Bean
  public JedisConnectionFactory jedisConnectionFactory(
    RedisProperties redisProperties
  ) {
    
    JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
    
    connectionFactory.setHostName(redisProperties.getHost());
    connectionFactory.setPort(redisProperties.getPort());
    connectionFactory.setPassword(redisProperties.getPassword());
    
    return connectionFactory;
  }
  
  @Bean
  public RedisTemplate<String, Session> redisTemplate(
    JedisConnectionFactory jedisConnectionFactory, ObjectMapper objectMapper
  ) {
    
    RedisTemplate<String, Session> redisTemplate = new RedisTemplate<>();
    
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new JsonSerializer(objectMapper));
    
    return redisTemplate;
  }
  
}
