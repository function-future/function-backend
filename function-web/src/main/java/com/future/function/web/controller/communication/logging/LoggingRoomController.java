package com.future.function.web.controller.communication.logging;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.logging.LogMessageService;
import com.future.function.service.api.feature.communication.logging.LoggingRoomService;
import com.future.function.service.api.feature.communication.logging.TopicService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.logging.LogMessageRequestMapper;
import com.future.function.web.mapper.request.communication.logging.LoggingRoomRequestMapper;
import com.future.function.web.mapper.request.communication.logging.TopicRequestMapper;
import com.future.function.web.mapper.response.communication.logging.LoggingRoomResponseMapper;
import com.future.function.web.model.request.communication.logging.LogMessageRequest;
import com.future.function.web.model.request.communication.logging.LoggingRoomRequest;
import com.future.function.web.model.request.communication.logging.TopicRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.logging.LogMessageResponse;
import com.future.function.web.model.response.feature.communication.logging.LoggingRoomResponse;
import com.future.function.web.model.response.feature.communication.logging.TopicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Ricky Kennedy
 * Created At : 0:19 28/07/2019
 */
@RestController
@RequestMapping(value= "/api/communication/logging-rooms")
public class LoggingRoomController {
  private LoggingRoomService loggingRoomService;

  private TopicService topicService;

  private LogMessageService logMessageService;

  private LogMessageRequestMapper logMessageRequestMapper;

  private TopicRequestMapper topicRequestMapper;

  private LoggingRoomRequestMapper loggingRoomRequestMapper;

  @Autowired
  public LoggingRoomController (
    LoggingRoomService loggingRoomService,
    TopicService topicService,
    LogMessageService logMessageService,
    LogMessageRequestMapper logMessageRequestMapper,
    TopicRequestMapper topicRequestMapper,
    LoggingRoomRequestMapper loggingRoomRequestMapper
  ){
    this.loggingRoomService = loggingRoomService;
    this.topicService = topicService;
    this.logMessageService = logMessageService;
    this.logMessageRequestMapper = logMessageRequestMapper;
    this.topicRequestMapper = topicRequestMapper;
    this.loggingRoomRequestMapper = loggingRoomRequestMapper;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<LoggingRoomResponse> getLoggingRoomsByMember(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT }) Session session,
    @RequestParam(required = false) String search,
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int size
  ) {
    if(search != null && !search.equals("")) {
      return LoggingRoomResponseMapper.toPagingLoggingRoomResponse(
        loggingRoomService.getLoggingRoomsByMemberWithKeyword(
          search,
          session.getUserId(),
          PageHelper.toPageable(page, size))
      );
    }
    return LoggingRoomResponseMapper.toPagingLoggingRoomResponse(
      loggingRoomService.getLoggingRoomsByMember(
        session.getUserId(),
        PageHelper.toPageable(page, size))
    );
  }

  @GetMapping(value = "/{loggingRoomId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<LoggingRoomResponse> getLoggingRoomDetail(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId
  ) {
    return LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(
      loggingRoomService.getLoggingRoom(loggingRoomId)
    );
  }

  @GetMapping(value = "/{loggingRoomId}/topics",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<TopicResponse> getLoggingRoomTopic(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId,
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return LoggingRoomResponseMapper.toPagingTopicResponse(
      topicService.getTopicByLoggingRoom(
        loggingRoomId, PageHelper.toPageable(page, size))
    );
  }

  @GetMapping(value = "/{loggingRoomId}/topics/{topicId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<TopicResponse> getLoggingRoomTopicDetail(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId
  ) {
    return LoggingRoomResponseMapper.toDataResponseTopicResponse(
      topicService.getTopic(topicId)
    );
  }

  @GetMapping(value = "/{loggingRoomId}/topics/{topicId}/log-messages",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<LogMessageResponse> getLogMessages(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId,
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return LoggingRoomResponseMapper.toPagingLogMessageResponse(
      logMessageService.getLogMessagesByTopic(
        topicId, PageHelper.toPageable(page, size))
    );
  }

  @PostMapping(
    value = "/{loggingRoomId}/topics/{topicId}/log-messages",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(value = HttpStatus.CREATED)
  public BaseResponse createLogMessage (
    @WithAnyRole(roles = Role.STUDENT ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId,
    @RequestBody LogMessageRequest logMessageRequest
    ) {

    logMessageService.createLogMessage(
      logMessageRequestMapper.toLogMessage(
        logMessageRequest, session.getId(), topicId)
    );

    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }

  @PostMapping(
    value = "/{loggingRoomId}/topics",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(value = HttpStatus.CREATED)
  public BaseResponse createTopic (
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId,
    @RequestBody TopicRequest topicRequest
  ) {
    topicService.createTopic(
      topicRequestMapper.toTopic(topicRequest, loggingRoomId, null)
    );
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(value = HttpStatus.CREATED)
  public BaseResponse createLoggingRoom (
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @RequestBody LoggingRoomRequest loggingRoomRequest
  ) {
    loggingRoomService.createLoggingRoom(
      loggingRoomRequestMapper.toLoggingRoom(loggingRoomRequest, null)
    );
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }

  @PutMapping(value = "/{loggingRoomId}/topics/{topicId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<TopicResponse> updateTopicDetail(
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId,
    @RequestBody TopicRequest topicRequest
  ) {
    return LoggingRoomResponseMapper.toDataResponseTopicResponse(
      topicService.updateTopic(
        topicRequestMapper.toTopic(topicRequest, loggingRoomId, topicId)
      )
    );
  }

  @PutMapping(value = "/{loggingRoomId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<LoggingRoomResponse> updateLoggingRoomDetail(
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId,
    @RequestBody LoggingRoomRequest loggingRoomRequest
  ) {
    return LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(
      loggingRoomService.updateLoggingRoom(
        loggingRoomRequestMapper.toLoggingRoom(loggingRoomRequest, loggingRoomId)
      )
    );
  }

  @DeleteMapping(value = "/{loggingRoomId}/topics/{topicId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteTopic(
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId
  ) {
    topicService.deleteTopic(topicId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @DeleteMapping(value = "/{loggingRoomId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteLoggingRoom(
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId
  ) {
    loggingRoomService.deleteLoggingRoom(loggingRoomId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
}
