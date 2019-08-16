package com.future.function.web;

import com.future.function.common.properties.core.FileProperties;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.session.model.Session;
import com.future.function.session.resolver.SessionResolver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static com.future.function.web.TestHelper.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootApplication(exclude = {
  MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
  MongoRepositoriesAutoConfiguration.class, RedisAutoConfiguration.class,
  RedisRepositoriesAutoConfiguration.class
})
@EnableConfigurationProperties(value = {
  FileProperties.class, SessionProperties.class
})
public class TestApplication {

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {

    return mock(JedisConnectionFactory.class);
  }

  @Bean
  public RedisTemplate<String, Session> redisTemplate(
    JedisConnectionFactory jedisConnectionFactory
  ) {

    RedisTemplate<String, Session> redisTemplate = mock(RedisTemplate.class);

    when(redisTemplate.getConnectionFactory()).thenReturn(
      jedisConnectionFactory);

    ValueOperations valueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    when(valueOperations.get(STUDENT_SESSION_ID)).thenReturn(STUDENT_SESSION);
    when(valueOperations.get(MENTOR_SESSION_ID)).thenReturn(MENTOR_SESSION);
    when(valueOperations.get(JUDGE_SESSION_ID)).thenReturn(JUDGE_SESSION);
    when(valueOperations.get(ADMIN_SESSION_ID)).thenReturn(ADMIN_SESSION);

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
