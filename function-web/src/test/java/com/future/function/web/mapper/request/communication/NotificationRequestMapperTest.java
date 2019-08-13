package com.future.function.web.mapper.request.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.NotificationRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationRequestMapperTest {

  @Mock
  private RequestValidator requestValidator;

  @InjectMocks
  private NotificationRequestMapper notificationRequestMapper;

  @After
  public void tearDown() {
    verifyNoMoreInteractions(requestValidator);
  }

  @Test
  public void testGivenNotificationRequestByCallingToNotificationReturnNotification() {
    NotificationRequest request = NotificationRequest.builder()
            .title("title").build();
    when(requestValidator.validate(request)).thenReturn(request);

    Notification notification = notificationRequestMapper.toNotification(request);

    assertThat(notification.getTitle()).isEqualTo("title");
    verify(requestValidator).validate(request);

  }

}
