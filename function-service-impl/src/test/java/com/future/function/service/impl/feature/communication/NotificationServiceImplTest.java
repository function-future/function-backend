package com.future.function.service.impl.feature.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.reminder.NotificationRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import com.future.function.session.model.Session;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Author: PriagungSatyagama
 * Created At: 21:09 10/07/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceImplTest {

  private static final String USER_ID = "userId";
  private static final User USER = User.builder().id(USER_ID).build();
  private static final PageRequest PAGEABLE = new PageRequest(0, 10);
  private static final Notification NOTIFICATION_1 = Notification.builder().id("notificationId1").member(USER).build();
  private static final Notification NOTIFICATION_2 = Notification.builder().id("notificationId2").member(USER).build();

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  @After
  public void tearDown() {
    verifyNoMoreInteractions(userService, notificationRepository);
  }

  @Test
  public void testGivenSessionByGettingNotificationsReturnPagedNotifications() {

    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(notificationRepository.findAllByMemberOrderByCreatedAtDesc(USER, PAGEABLE))
            .thenReturn(PageHelper.toPage(Arrays.asList(NOTIFICATION_1, NOTIFICATION_2), PAGEABLE));

    Page<Notification> notifications = notificationService
            .getNotifications(Session.builder().userId(USER_ID).build(), PAGEABLE);

    assertThat(notifications).isNotNull();
    assertThat(notifications.getTotalElements()).isEqualTo(2);
    assertThat(notifications.getContent().get(0).getId()).isEqualTo(NOTIFICATION_1.getId());
    assertThat(notifications.getContent().get(1).getId()).isEqualTo(NOTIFICATION_2.getId());

    verify(userService).getUser(USER_ID);
    verify(notificationRepository).findAllByMemberOrderByCreatedAtDesc(USER, PAGEABLE);
  }

  @Test
  public void testGivenNotificationByCreatingNotificationReturnNotification() {

    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(notificationRepository.save(NOTIFICATION_1))
            .thenReturn(NOTIFICATION_1);

    Notification notification = notificationService
            .createNotification(NOTIFICATION_1);

    assertThat(notification).isNotNull();
    assertThat(notification.getId()).isEqualTo(NOTIFICATION_1.getId());
    assertThat(notification.getMember().getId()).isEqualTo(USER_ID);

    verify(userService).getUser(USER_ID);
    verify(notificationRepository).save(NOTIFICATION_1);
  }

}
