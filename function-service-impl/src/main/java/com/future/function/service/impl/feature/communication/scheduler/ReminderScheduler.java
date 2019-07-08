package com.future.function.service.impl.feature.communication.scheduler;

import com.future.function.common.properties.communication.ReminderProperties;
import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.service.api.feature.communication.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Author: PriagungSatyagama
 * Created At: 13:21 07/07/2019
 */
@Component
@Slf4j
public class ReminderScheduler {

  private final ReminderProperties reminderProperties;

  private final ReminderService reminderService;

  private final NotificationService notificationService;

  @Autowired
  public ReminderScheduler(ReminderProperties reminderProperties, ReminderService reminderService, NotificationService notificationService) {
    this.reminderProperties = reminderProperties;
    this.reminderService = reminderService;
    this.notificationService = notificationService;
  }

  @Scheduled(fixedDelayString = "#{@reminderProperties.schedulerPeriod}")
  public void createNotificationBasedOnReminder() {
    LocalDateTime timeNow = LocalDateTime.now();
    System.out.println(timeNow.getSecond());
    reminderService.getAllReminder().forEach(reminder -> generateNotificationIfNecessary(reminder, timeNow));
  }

  private void generateNotificationIfNecessary(Reminder reminder, LocalDateTime timeNow) {
    LocalDateTime lastReminderTime = millisToLocalDateTime(reminder.getLastReminderSent());
    if ((reminder.getIsRepeatedMonthly() &&
            isItNowToSendReminderMonthly(reminder, timeNow, lastReminderTime)) ||
        (!reminder.getIsRepeatedMonthly() &&
            isItNowToSendReminderWeekly(reminder, timeNow, lastReminderTime))) {
      sendNotifications(reminder);
    }
  }

  private Boolean isItTimeToSendReminder(Reminder reminder, LocalDateTime time) {
    return reminder.getHour() == time.getHour() && reminder.getMinute() == time.getMinute();
  }

  private Boolean isItNowToSendReminderMonthly(Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime) {
    return isItTimeToSendReminder(reminder, timeNow) &&
            (lastReminderTime != null &&
            timeNow.getMonth() != lastReminderTime.getMonth() &&
            timeNow.getDayOfMonth() == reminder.getMonthlyDate()) ||
            (lastReminderTime == null && timeNow.getDayOfMonth() == reminder.getMonthlyDate());
  }

  private Boolean isItNowToSendReminderWeekly(Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime) {
    return lastReminderTime.getDayOfWeek() != timeNow.getDayOfWeek() &&
            reminder.getDays().contains(timeNow.getDayOfWeek()) &&
            isItTimeToSendReminder(reminder, timeNow);
  }

  private LocalDateTime millisToLocalDateTime(Long millis) {
    if (millis == null) {
      return null;
    }
    Instant instant = Instant.ofEpochMilli(millis);
    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  private void sendNotifications(Reminder reminder) {
    reminder.getMembers().forEach(member -> {
      notificationService.createNotification(Notification.builder()
              .content(reminder.getContent())
              .title(reminder.getTitle())
              .member(member)
              .build());
    });
    reminder.setLastReminderSent(Instant.now().toEpochMilli());
    reminderService.updateReminder(reminder);
  }

}
