package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.communication.NotificationRequestMapper;
import com.future.function.web.mapper.response.communication.NotificationResponseMapper;
import com.future.function.web.model.request.communication.NotificationRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Author: PriagungSatyagama
 * Created At: 21:11 06/07/2019
 */
@RestController
@RequestMapping(value = "/api/communication/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  private final NotificationRequestMapper notificationRequestMapper;

  @Autowired
  public NotificationController(NotificationService notificationService, NotificationRequestMapper notificationRequestMapper) {
    this.notificationService = notificationService;
    this.notificationRequestMapper = notificationRequestMapper;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<NotificationResponse> getNotifications(
          @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
          Session session,
          @RequestParam(defaultValue = "1", required = false)
          int page,
          @RequestParam(defaultValue = "10", required = false)
          int size
  ) {
    return NotificationResponseMapper.toPagingNotificationResponse(
            notificationService.getNotifications(session, PageHelper.toPageable(page, size)));
  }

  @PostMapping(
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<NotificationResponse> createNotification(
          @WithAnyRole(roles = Role.ADMIN) Session session,
          @RequestBody NotificationRequest data) {
    return NotificationResponseMapper.toSingleNotificationResponse(
            notificationService.createNotification(notificationRequestMapper.toNotification(data)));
  }

}
