package com.future.function.service.impl.feature.communication.scheduler;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.service.api.feature.communication.ReminderService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Author: PriagungSatyagama
 * Created At: 23:00 10/07/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class ReminderSchedulerTest {

  private static final String USER_ID_1 = "userId1";
  private static final String USER_ID_2 = "userId2";
  private static final User USER_1 = User.builder().id(USER_ID_1).build();
  private static final User USER_2 = User.builder().id(USER_ID_2).build();
  private static final String REMINDER_ID_1 = "reminderId1";
  private static final String REMINDER_ID_2 = "reminderId2";
  private static final Reminder REMINDER_1 = Reminder.builder()
          .id(REMINDER_ID_1)
          .isRepeatedMonthly(true)
          .hour(LocalDateTime.now().getHour())
          .monthlyDate(LocalDateTime.now().getDayOfMonth())
          .minute(LocalDateTime.now().getMinute())
          .members(Arrays.asList(USER_1, USER_2))
          .build();
  private static final Reminder REMINDER_2 = Reminder.builder()
          .id(REMINDER_ID_2)
          .isRepeatedMonthly(false)
          .hour(LocalDateTime.now().getHour())
          .minute(LocalDateTime.now().getMinute())
          .days(Collections.singletonList(LocalDateTime.now().getDayOfWeek()))
          .members(Arrays.asList(USER_1, USER_2))
          .build();
  @Mock
  private ReminderService reminderService;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private ReminderScheduler reminderScheduler;

  @After
  public void tearDown() {
    verifyNoMoreInteractions(reminderService, notificationService);
  }

  @Test
  public void testGivenMethodCallByCallingCreateNotificationBasedOnReminderMonthly() {
    when(notificationService.createNotification(any(Notification.class))).thenReturn(Notification.builder().build());
    when(reminderService.getAllReminder()).thenReturn(Collections.singletonList(REMINDER_1));

    reminderScheduler.createNotificationBasedOnReminder();

    verify(reminderService).getAllReminder();
    verify(notificationService, times(2)).createNotification(any(Notification.class));
    verify(reminderService).updateReminder(REMINDER_1);
  }

  @Test
  public void testGivenMethodCallByCallingCreateNotificationBasedOnReminderWeekly() {
    when(notificationService.createNotification(any(Notification.class))).thenReturn(Notification.builder().build());
    when(reminderService.getAllReminder()).thenReturn(Collections.singletonList(REMINDER_2));

    reminderScheduler.createNotificationBasedOnReminder();

    verify(reminderService).getAllReminder();
    verify(notificationService, times(2)).createNotification(any(Notification.class));
    verify(reminderService).updateReminder(REMINDER_2);
  }

}
