package com.future.function.web.mapper.response.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationTotalUnseenResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;



@RunWith(MockitoJUnitRunner.class)
public class NotificationResponseMapperTest {

  private static final User USER = User.builder().id("userId").build();
  private static final String NOTIFICATION_ID_1 = "notificationId1";
  private static final String NOTIFICATION_ID_2 = "notificationId2";
  private static final Notification NOTIFICATION_1 = Notification.builder().member(USER).id(NOTIFICATION_ID_1).build();
  private static final Notification NOTIFICATION_2 = Notification.builder().member(USER).id(NOTIFICATION_ID_2).build();

  @Test
  public void testGivenPageNotificationsByCallingToPagingNotificationReturnPagingResponse() {
    Page<Notification> notificationPage = new PageImpl<>(
            Arrays.asList(NOTIFICATION_1, NOTIFICATION_2), PageHelper.toPageable(1, 10), 2);
    PagingResponse<NotificationResponse> responses = NotificationResponseMapper.toPagingNotificationResponse(notificationPage);
    assertThat(responses.getData().get(0).getId()).isEqualTo(NOTIFICATION_1.getId());
    assertThat(responses.getData().get(1).getId()).isEqualTo(NOTIFICATION_2.getId());
  }

  @Test
  public void testGivenNotificationByCallingToSingleNotificationReturnDataResponse() {
    DataResponse<NotificationResponse> response = NotificationResponseMapper.toSingleNotificationResponse(NOTIFICATION_1);
    assertThat(response.getData().getId()).isEqualTo(NOTIFICATION_ID_1);
  }

  @Test
  public void testGivenIntegerByCallingToNotificationTotalUnseenResponseReturnDataResponse() {
    int total = 1;
    DataResponse<NotificationTotalUnseenResponse> response = NotificationResponseMapper.toNotificationTotalUnseenResponse(total);
    assertThat(response.getData().getTotal()).isEqualTo(total);
    assertThat(response.getCode()).isEqualTo(200);
  }

}
