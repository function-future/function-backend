package com.future.function.web.controller.communication.mq;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.connection.MessageListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class RedisListenerFactoryTest {

  @Mock
  ChatRedisListener chatRedisListener;

  @Mock
  NotificationRedisListener notificationRedisListener;

  @InjectMocks
  RedisListenerFactory listenerFactory;

  @Test
  public void testGivenChatTopicByGettingMessageListenerReturnMessageListener() {
    ChatRedisListener listener = (ChatRedisListener) listenerFactory.getMessageListener("chat");
    assertThat(listener).isEqualTo(chatRedisListener);
  }

  @Test
  public void testGivenNotificationTopicByGettingMessageListenerReturnMessageListener() {
    NotificationRedisListener listener = (NotificationRedisListener) listenerFactory.getMessageListener("notification");
    assertThat(listener).isEqualTo(notificationRedisListener);
  }

  @Test
  public void testGivenEmptyTopicByGettingMessageListenerReturnMessageListener() {
    MessageListener listener = listenerFactory.getMessageListener("");
    assertThat(listener).isEqualTo(null);
  }

}
