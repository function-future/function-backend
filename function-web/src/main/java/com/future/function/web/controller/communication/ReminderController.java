package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.ReminderService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.ReminderRequestMapper;
import com.future.function.web.mapper.response.communication.ReminderResponseMapper;
import com.future.function.web.model.request.communication.ReminderRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderDetailResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Author: PriagungSatyagama
 * Created At: 21:46 06/07/2019
 */
@RestController
@RequestMapping(value = "/api/communication/reminders")
@WithAnyRole(roles = Role.ADMIN)
public class ReminderController {

  private final ReminderRequestMapper reminderRequestMapper;

  private final ReminderService reminderService;

  @Autowired
  public ReminderController(ReminderRequestMapper reminderRequestMapper, ReminderService reminderService) {
    this.reminderRequestMapper = reminderRequestMapper;
    this.reminderService = reminderService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<ReminderResponse> getReminders(
          Session session,
          @RequestParam(required = false, defaultValue = "1") int page,
          @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return ReminderResponseMapper.toPagingReminderResponse(reminderService
            .getAllPagedReminder(PageHelper.toPageable(page, size)));
  }

  @GetMapping(
          value = "/{reminderId:.+}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReminderDetailResponse> getReminder(Session session,
          @PathVariable String reminderId) {
    return ReminderResponseMapper
            .toSingleReminderDataResponse(reminderService.getReminder(reminderId));
  }

  @PostMapping(
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReminderDetailResponse> createReminder(
          Session session, @RequestBody ReminderRequest request) {
    return ReminderResponseMapper.toSingleReminderDataResponse(reminderService
            .createReminder(reminderRequestMapper.toReminder(request, null)));
  }

  @PutMapping(
          value = "/{reminderId:.+}",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReminderDetailResponse> updateReminder(
          Session session, @RequestBody ReminderRequest request, @PathVariable String reminderId) {
    return ReminderResponseMapper.toSingleReminderDataResponse(reminderService
            .updateReminder(reminderRequestMapper.toReminder(request, reminderId)));
  }

  @DeleteMapping(
          value = "/{reminderId:.+}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteReminder(
          Session session, @PathVariable String reminderId) {
    reminderService.deleteReminder(reminderId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
