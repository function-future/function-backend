package com.future.function.web.mapper.request.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: PriagungSatyagama
 * Created At: 21:14 06/07/2019
 */
@Slf4j
@Component
public class NotificationRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public NotificationRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public Notification toNotification(NotificationRequest request) {
    validator.validate(request);

    return Notification.builder()
            .content(request.getDescription())
            .title(request.getTitle())
            .member(User.builder().id(request.getTargetUser()).build())
            .build();
  }
}
