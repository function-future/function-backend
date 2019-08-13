package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/communication/reminders")
@WithAnyRole(roles = Role.ADMIN)
public class ReminderController {

  private final ReminderRequestMapper reminderRequestMapper;

  private final ReminderService reminderService;

  private final FileProperties fileProperties;

  @Autowired
  public ReminderController(ReminderRequestMapper reminderRequestMapper, ReminderService reminderService, FileProperties fileProperties) {
    this.reminderRequestMapper = reminderRequestMapper;
    this.reminderService = reminderService;
    this.fileProperties = fileProperties;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<ReminderResponse> getReminders(
          Session session,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "") String search
  ) {
    return ReminderResponseMapper.toPagingReminderResponse(reminderService
            .getAllPagedReminder(PageHelper.toPageable(page, size), search));
  }

  @GetMapping(
          value = "/{reminderId:.+}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReminderDetailResponse> getReminder(Session session,
          @PathVariable String reminderId) {
    return ReminderResponseMapper
            .toSingleReminderDataResponse(reminderService.getReminder(reminderId), fileProperties.getUrlPrefix());
  }

  @PostMapping(
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReminderDetailResponse> createReminder(
          Session session, @RequestBody ReminderRequest request) {
    return ReminderResponseMapper.toSingleReminderDataResponse(reminderService
            .createReminder(reminderRequestMapper.toReminder(request, null)), fileProperties.getUrlPrefix());
  }

  @PutMapping(
          value = "/{reminderId:.+}",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReminderDetailResponse> updateReminder(
          Session session, @RequestBody ReminderRequest request, @PathVariable String reminderId) {
    return ReminderResponseMapper.toSingleReminderDataResponse(reminderService
            .updateReminder(reminderRequestMapper.toReminder(request, reminderId)), fileProperties.getUrlPrefix());
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
