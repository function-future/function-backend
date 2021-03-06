package com.future.function.web.mapper.response.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationTotalUnseenResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponseMapper {

  public static PagingResponse<NotificationResponse> toPagingNotificationResponse(
    Page<Notification> notifications
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toListNotificationResponse(
                                             notifications),
                                           PageHelper.toPaging(notifications)
    );
  }

  private static List<NotificationResponse> toListNotificationResponse(
    Page<Notification> notifications
  ) {

    return notifications.getContent()
      .stream()
      .map(NotificationResponseMapper::toNotificationResponse)
      .collect(Collectors.toList());
  }

  private static NotificationResponse toNotificationResponse(
    Notification notification
  ) {

    return NotificationResponse.builder()
      .id(notification.getId())
      .createdAt(notification.getCreatedAt())
      .description(notification.getContent())
      .targetUser(notification.getMember()
                    .getId())
      .title(notification.getTitle())
      .build();
  }

  public static DataResponse<NotificationResponse> toSingleNotificationResponse(
    Notification notification
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toNotificationResponse(notification));
  }

  public static DataResponse<NotificationTotalUnseenResponse> toNotificationTotalUnseenResponse(
    Integer total
  ) {

    return ResponseHelper.toDataResponse(HttpStatus.OK,
                                         NotificationTotalUnseenResponse.builder()
                                           .total(total)
                                           .build()
    );
  }

}
