package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
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
import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceImplTest {

  private static final String USER_ID = "userId";

  public static final Session SESSION = Session.builder()
    .userId(USER_ID)
    .build();

  private static final User USER = User.builder()
    .id(USER_ID)
    .build();

  private static final PageRequest PAGEABLE = new PageRequest(0, 10);

  private Notification NOTIFICATION_1 = Notification.builder()
    .id("notificationId1")
    .member(USER)
    .seen(false)
    .build();

  private Notification NOTIFICATION_3 = Notification.builder()
    .id("notificationId3")
    .member(USER)
    .seen(false)
    .build();

  private Notification NOTIFICATION_4 = Notification.builder()
    .id("notificationId4")
    .member(USER)
    .seen(false)
    .build();

  private Notification NOTIFICATION_2 = Notification.builder()
    .id("notificationId2")
    .member(USER)
    .seen(true)
    .build();

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
    when(notificationRepository.findAllByMemberOrderByCreatedAtDesc(USER,
                                                                    PAGEABLE
    )).thenReturn(
      PageHelper.toPage(Arrays.asList(NOTIFICATION_1, NOTIFICATION_2),
                        PAGEABLE
      ));

    Page<Notification> notifications = notificationService.getNotifications(
      SESSION, PAGEABLE);

    assertThat(notifications).isNotNull();
    assertThat(notifications.getTotalElements()).isEqualTo(2);
    assertThat(notifications.getContent()
                 .get(0)
                 .getId()).isEqualTo(NOTIFICATION_1.getId());
    assertThat(notifications.getContent()
                 .get(1)
                 .getId()).isEqualTo(NOTIFICATION_2.getId());

    verify(userService).getUser(USER_ID);
    verify(notificationRepository).findAllByMemberOrderByCreatedAtDesc(
      USER, PAGEABLE);
  }

  @Test
  public void testGivenNotificationByCreatingNotificationReturnNotification() {

    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(notificationRepository.save(NOTIFICATION_1)).thenReturn(
      NOTIFICATION_1);

    Notification notification = notificationService.createNotification(
      NOTIFICATION_1);

    assertThat(notification).isNotNull();
    assertThat(notification.getId()).isEqualTo(NOTIFICATION_1.getId());
    assertThat(notification.getMember()
                 .getId()).isEqualTo(USER_ID);

    verify(userService).getUser(USER_ID);
    verify(notificationRepository).save(NOTIFICATION_1);
  }

  @Test
  public void testGivenSessionByGettingTotalUnseenNotificationsReturnInteger() {

    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(notificationRepository.findAllByMemberAndSeen(USER, false)).thenReturn(
      Collections.singletonList(NOTIFICATION_1));

    Integer result = notificationService.getTotalUnseenNotifications(SESSION);

    assertThat(result).isEqualTo(1);
    verify(userService).getUser(USER_ID);
    verify(notificationRepository).findAllByMemberAndSeen(USER, false);

  }

  @Test
  public void testGivenNotificationByUpdatingSeenStatusReturnNotificationSuccess() {

    NOTIFICATION_1.setCreatedAt(1L);
    NOTIFICATION_4.setCreatedAt(3L);
    NOTIFICATION_3.setCreatedAt(2L);
    when(
      notificationRepository.findAllByMemberAndSeen(NOTIFICATION_3.getMember(),
                                                    false
      )).thenReturn(
      Arrays.asList(NOTIFICATION_1, NOTIFICATION_4, NOTIFICATION_3));
    when(notificationRepository.findOne("notificationId1")).thenReturn(
      NOTIFICATION_1);
    when(notificationRepository.findOne("notificationId3")).thenReturn(
      NOTIFICATION_3);
    when(notificationRepository.save(NOTIFICATION_1)).thenReturn(
      NOTIFICATION_1);
    when(notificationRepository.save(NOTIFICATION_3)).thenReturn(
      NOTIFICATION_3);

    notificationService.updateSeenNotification(NOTIFICATION_3.getId());

    verify(notificationRepository).findAllByMemberAndSeen(
      NOTIFICATION_3.getMember(), false);
    verify(notificationRepository).findOne("notificationId1");
    verify(notificationRepository, times(2)).findOne("notificationId3");
    verify(notificationRepository).save(NOTIFICATION_1);
    verify(notificationRepository).save(NOTIFICATION_3);
  }

  @Test
  public void testGivenNotificationByUpdatingSeenStatusReturnNotFoundException() {

    when(notificationRepository.findOne("notificationId3")).thenReturn(null);

    catchException(
      () -> notificationService.updateSeenNotification(NOTIFICATION_3.getId()));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    verify(notificationRepository).findOne("notificationId3");

  }

}
