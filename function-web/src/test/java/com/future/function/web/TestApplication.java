package com.future.function.web;

import com.future.function.common.properties.core.SessionProperties;
import com.future.function.session.model.Session;
import com.future.function.session.resolver.SessionResolver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication(exclude = {
  MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
  MongoRepositoriesAutoConfiguration.class
})
public class TestApplication {
  
  @Bean
  public SessionProperties sessionProperties() {
    
    return new SessionProperties(108000, 108000, "Function-Session");
  }
  
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
  
  @Bean
  public SessionResolver sessionResolver(
    RedisTemplate<String, Session> redisTemplate,
    SessionProperties sessionProperties
  ) {
    
    return new SessionResolver(redisTemplate, sessionProperties);
  }
  
}
