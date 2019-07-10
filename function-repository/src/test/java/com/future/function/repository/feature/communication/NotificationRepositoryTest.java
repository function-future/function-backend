package com.future.function.repository.feature.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.communication.reminder.NotificationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: PriagungSatyagama
 * Created At: 20:50 10/07/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class NotificationRepositoryTest {

  private static final String NOTIFICATION_ID_2 = "notificationId2";
  private static final String NOTIFICATION_ID_1 = "notificationId1";
  private static final User MEMBER = User.builder().id("userId").build();
  private static final PageRequest PAGEABLE = new PageRequest(0, 2);

  @Autowired
  private NotificationRepository notificationRepository;

  @Before
  public void setUp() {

    Notification notification1 = Notification.builder()
            .id(NOTIFICATION_ID_1)
            .member(MEMBER)
            .build();
    notification1.setCreatedAt(1L);

    Notification notification2 = Notification.builder()
            .id(NOTIFICATION_ID_2)
            .member(MEMBER)
            .build();
    notification2.setCreatedAt(2L);

    notificationRepository.save(notification1);
    notificationRepository.save(notification2);

  }

  @After
  public void tearDown() {notificationRepository.deleteAll();}

  @Test
  public void testGivenMemberByFindingAllMemberReturnPagedNotifications() {
    Page<Notification> notifications = notificationRepository.findAllByMemberOrderByCreatedAtDesc(MEMBER, PAGEABLE);

    assertThat(notifications).isNotNull();
    assertThat(notifications.getContent().get(0).getId()).isEqualTo(NOTIFICATION_ID_2);
    assertThat(notifications.getContent().get(1).getId()).isEqualTo(NOTIFICATION_ID_1);
  }

}
