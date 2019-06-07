package com.future.function.web.configuration;

import com.future.function.session.model.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
  
  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    
    return new JedisConnectionFactory();
  }
  
  @Bean
  public RedisTemplate<String, Session> redisTemplate(
    JedisConnectionFactory jedisConnectionFactory
  ) {
    
    RedisTemplate<String, Session> redisTemplate = new RedisTemplate<>();
    
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    
    return redisTemplate;
  }
  
}
