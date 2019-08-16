package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.NotificationRequestMapper;
import com.future.function.web.mapper.response.communication.NotificationResponseMapper;
import com.future.function.web.model.request.communication.NotificationRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationTotalUnseenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/communication/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  private final NotificationRequestMapper notificationRequestMapper;

  @Autowired
  public NotificationController(
    NotificationService notificationService,
    NotificationRequestMapper notificationRequestMapper
  ) {

    this.notificationService = notificationService;
    this.notificationRequestMapper = notificationRequestMapper;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<NotificationResponse> getNotifications(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestParam(defaultValue = "1",
                  required = false)
      int page,
    @RequestParam(defaultValue = "10",
                  required = false)
      int size
  ) {

    return NotificationResponseMapper.toPagingNotificationResponse(
      notificationService.getNotifications(session,
                                           PageHelper.toPageable(page, size)
      ));
  }

  @GetMapping(value = "/_unseen_total",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<NotificationTotalUnseenResponse> getTotalUnseen(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session
  ) {

    return NotificationResponseMapper.toNotificationTotalUnseenResponse(
      notificationService.getTotalUnseenNotifications(session));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
               consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<NotificationResponse> createNotification(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestBody
      NotificationRequest data
  ) {

    return NotificationResponseMapper.toSingleNotificationResponse(
      notificationService.createNotification(
        notificationRequestMapper.toNotification(data)));
  }

  @PutMapping(value = "/{notificationId:.+}/_read",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse readNotification(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @PathVariable
      String notificationId
  ) {

    notificationService.updateSeenNotification(notificationId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
