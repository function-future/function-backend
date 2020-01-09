package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.response.communication.NotificationResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationTotalUnseenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationRedisListener implements BaseListener {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  SimpMessagingTemplate messagingTemplate;

  @Autowired
  NotificationService notificationService;

  @Override
  public void setContextHolder(String userId) {}

  @Override
  public void onMessage(Message message, byte[] bytes) {
    try {
      String userId = objectMapper.readValue(message.getBody(), String.class);
      Integer total = notificationService.getTotalUnseenNotifications(Session.builder().userId(userId).build());
      DataResponse<NotificationTotalUnseenResponse> data = NotificationResponseMapper
              .toNotificationTotalUnseenResponse(total);
      messagingTemplate.convertAndSend("/topic/users/" + userId + "/notifications", data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
