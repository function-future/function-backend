package com.future.function.service.impl.feature.communication.scheduler;

import com.future.function.common.properties.communication.ReminderProperties;
import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.service.api.feature.communication.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ReminderScheduler {

  private final ReminderProperties reminderProperties;

  private final ReminderService reminderService;

  private final NotificationService notificationService;

  @Autowired
  public ReminderScheduler(
    ReminderProperties reminderProperties, ReminderService reminderService,
    NotificationService notificationService
  ) {

    this.reminderProperties = reminderProperties;
    this.reminderService = reminderService;
    this.notificationService = notificationService;
  }

  @Scheduled(fixedDelayString = "#{@reminderProperties.schedulerPeriod}")
  public void createNotificationBasedOnReminder() {

    LocalDateTime timeNow = Instant.now()
      .atZone(ZoneId.of("Asia/Jakarta"))
      .toLocalDateTime();
    reminderService.getAllReminder()
      .forEach(reminder -> generateNotificationIfNecessary(reminder, timeNow));
  }

  private void generateNotificationIfNecessary(
    Reminder reminder, LocalDateTime timeNow
  ) {

    LocalDateTime lastReminderTime = millisToLocalDateTime(
      reminder.getLastReminderSent());
    if (monthlyCheck(reminder, timeNow, lastReminderTime) || weeklyCheck(
      reminder, timeNow, lastReminderTime)) {
      sendNotifications(reminder);
    }
  }

  private boolean weeklyCheck(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return !reminder.getIsRepeatedMonthly() && isItNowToSendReminderWeekly(
      reminder, timeNow, lastReminderTime);
  }

  private Boolean isItNowToSendReminderWeekly(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return isItTimeToSendReminder(reminder, timeNow) && (
      weeklyFirstValid(reminder, timeNow, lastReminderTime) || weeklyValid(
        reminder, timeNow, lastReminderTime)
    );
  }

  private boolean weeklyValid(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return (
             lastReminderTime != null &&
             lastReminderTime.getDayOfWeek() != timeNow.getDayOfWeek()
           ) && reminder.getDays()
             .contains(timeNow.getDayOfWeek());
  }

  private boolean weeklyFirstValid(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return lastReminderTime == null && reminder.getDays()
      .contains(timeNow.getDayOfWeek());
  }

  private boolean monthlyCheck(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return reminder.getIsRepeatedMonthly() && isItNowToSendReminderMonthly(
      reminder, timeNow, lastReminderTime);
  }

  private Boolean isItNowToSendReminderMonthly(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return isItTimeToSendReminder(reminder, timeNow) && monthlyValid(
      reminder, timeNow, lastReminderTime) || monthlyFirstValid(
      reminder, timeNow, lastReminderTime);
  }

  private Boolean isItTimeToSendReminder(
    Reminder reminder, LocalDateTime time
  ) {

    return reminder.getHour() == time.getHour() &&
           reminder.getMinute() == time.getMinute();
  }

  private boolean monthlyFirstValid(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return lastReminderTime == null &&
           timeNow.getDayOfMonth() == reminder.getMonthlyDate();
  }

  private boolean monthlyValid(
    Reminder reminder, LocalDateTime timeNow, LocalDateTime lastReminderTime
  ) {

    return lastReminderTime != null &&
           timeNow.getMonth() != lastReminderTime.getMonth() &&
           timeNow.getDayOfMonth() == reminder.getMonthlyDate();
  }

  private LocalDateTime millisToLocalDateTime(Long millis) {

    if (millis == null) {
      return null;
    }
    Instant instant = Instant.ofEpochMilli(millis);
    return instant.atZone(ZoneId.of("Asia/Jakarta"))
      .toLocalDateTime();
  }

  private void sendNotifications(Reminder reminder) {

    Authentication auth = new UsernamePasswordAuthenticationToken(
      "system", "system");
    SecurityContextHolder.getContext()
      .setAuthentication(auth);
    reminder.getMembers()
      .forEach(member -> notificationService.createNotification(
        Notification.builder()
          .content(reminder.getContent())
          .title(reminder.getTitle())
          .member(member)
          .build()));
    reminder.setLastReminderSent(Instant.now()
                                   .toEpochMilli());
    reminderService.updateReminder(reminder);
  }

}
