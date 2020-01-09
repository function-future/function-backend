package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.response.communication.NotificationResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationTotalUnseenResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationRedisListenerTest {

  @Mock
  ObjectMapper objectMapper;

  @Mock
  SimpMessagingTemplate simpMessagingTemplate;

  @Mock
  NotificationService notificationService;

  @InjectMocks
  NotificationRedisListener notificationRedisListener;

  String userId = "userId";

  Integer total = 0;

  Message message = new Message() {
    @Override
    public byte[] getBody() {
      return new byte[0];
    }

    @Override
    public byte[] getChannel() {
      return new byte[0];
    }
  };

  byte[] bytes = new byte[0];

  DataResponse<NotificationTotalUnseenResponse> response = NotificationResponseMapper.toNotificationTotalUnseenResponse(total);

  @After
  public void tearDown() {
    verifyNoMoreInteractions(objectMapper, simpMessagingTemplate, notificationService);
  }

  @Test
  public void testGivenMessageAndByteArrayByCallingOnMessageReturnVoid() throws IOException {
    when(objectMapper.readValue(message.getBody(), String.class)).thenReturn(userId);
    when(notificationService.getTotalUnseenNotifications(any(Session.class))).thenReturn(total);
    doNothing().when(simpMessagingTemplate).convertAndSend("/topic/users/" + userId + "/notifications", response);

    notificationRedisListener.onMessage(message, bytes);

    verify(objectMapper).readValue(message.getBody(), String.class);
    verify(notificationService).getTotalUnseenNotifications(any(Session.class));
    verify(simpMessagingTemplate).convertAndSend("/topic/users/" + userId + "/notifications", response);
  }

}
