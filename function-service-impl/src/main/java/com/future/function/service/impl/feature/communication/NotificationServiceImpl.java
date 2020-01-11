package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.reminder.NotificationRepository;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.service.api.feature.communication.mq.MessagePublisherService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  private final UserService userService;

  private final MessagePublisherService publisherService;

  @Value("${function.mq.topic.notification}")
  private String mqTopicNotification;

  @Autowired
  public NotificationServiceImpl(
          NotificationRepository notificationRepository, UserService userService,
          MessagePublisherService publisherService) {

    this.notificationRepository = notificationRepository;
    this.userService = userService;
    this.publisherService = publisherService;
  }

  @Override
  public Integer getTotalUnseenNotifications(Session session) {

    return Optional.of(session.getUserId())
      .map(userService::getUser)
      .map(user -> notificationRepository.findAllByMemberAndSeen(user, false))
      .orElse(new ArrayList<>())
      .size();
  }

  @Override
  public Page<Notification> getNotifications(
    Session session, Pageable pageable
  ) {

    return Optional.of(session.getUserId())
      .map(userService::getUser)
      .map(
        user -> notificationRepository.findAllByMemberOrderByCreatedAtDesc(user,
                                                                           pageable
        ))
      .orElse(PageHelper.empty(pageable));
  }

  @Override
  public Notification createNotification(Notification notification) {

    return Optional.of(notification)
      .map(this::setMember)
      .map(n -> this.setSeen(n, false))
      .map(notificationRepository::save)
      .map(n -> {
        this.publishNotification(n);
        return n;
      })
      .orElseThrow(UnsupportedOperationException::new);
  }

  private void publishNotification(Notification notification) {
    publisherService.publish(notification.getMember().getId(), mqTopicNotification);
  }

  @Override
  public void updateSeenNotification(String notificationId) {

    Notification notification = Optional.of(notificationId)
      .map(notificationRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Notification not found"));
    notificationRepository.findAllByMemberAndSeen(
      notification.getMember(), false)
      .stream()
      .filter(n -> n.getCreatedAt() <= notification.getCreatedAt())
      .forEach(this::updateSingleNotificationSeenStatus);
  }

  private void updateSingleNotificationSeenStatus(Notification notification) {

    Optional.of(notification.getId())
      .map(notificationRepository::findOne)
      .map(n -> this.setSeen(n, true))
      .ifPresent(notificationRepository::save);
  }

  private Notification setMember(Notification notification) {

    User member = Optional.of(notification.getMember()
                                .getId())
      .map(userService::getUser)
      .orElseThrow(() -> new NotFoundException("User not found"));
    notification.setMember(member);
    return notification;
  }

  private Notification setSeen(Notification notification, Boolean value) {

    notification.setSeen(value);
    return notification;
  }

}
