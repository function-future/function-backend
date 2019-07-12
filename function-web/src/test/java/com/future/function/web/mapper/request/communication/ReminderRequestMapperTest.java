package com.future.function.web.mapper.request.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.NotificationRequest;
import com.future.function.web.model.request.communication.ReminderRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Author: PriagungSatyagama
 * Created At: 0:18 11/07/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class ReminderRequestMapperTest {

  @Mock
  private RequestValidator requestValidator;

  @InjectMocks
  private ReminderRequestMapper reminderRequestMapper;

  @After
  public void tearDown() {
    verifyNoMoreInteractions(requestValidator);
  }

  @Test
  public void testGivenNotificationRequestByCallingToNotificationReturnNotification() {
    ReminderRequest request = ReminderRequest.builder()
            .repeatDays(Collections.singletonList(DayOfWeek.FRIDAY.name()))
            .members(Collections.singletonList("userId"))
            .build();
    when(requestValidator.validate(request)).thenReturn(request);

    Reminder reminder = reminderRequestMapper.toReminder(request, "reminderId");

    assertThat(reminder.getId()).isEqualTo("reminderId");
    assertThat(reminder.getMembers().get(0).getId()).isEqualTo("userId");
    verify(requestValidator).validate(request);

  }

}
