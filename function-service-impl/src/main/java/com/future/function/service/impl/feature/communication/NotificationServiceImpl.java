package com.future.function.service.impl.feature.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.reminder.NotificationRepository;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Author: PriagungSatyagama
 * Created At: 15:21 06/07/2019
 */
@Service
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  private final UserService userService;

  @Autowired
  public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
    this.notificationRepository = notificationRepository;
    this.userService = userService;
  }

  @Override
  public Page<Notification> getNotifications(Session session, Pageable pageable) {
    return Optional.of(session.getUserId())
            .map(userService::getUser)
            .map(user -> notificationRepository.findAllByMemberOrderByCreatedAtDesc(user, pageable))
            .orElse(PageHelper.empty(pageable));
  }

  @Override
  public Notification createNotification(Notification notification) {
    return Optional.of(notification)
            .map(this::setMember)
            .map(notificationRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  private Notification setMember(Notification notification) {
    User member = Optional.of(notification.getMember().getId())
            .map(userService::getUser)
            .orElse(null);
    notification.setMember(member);
    return notification;
  }
}
