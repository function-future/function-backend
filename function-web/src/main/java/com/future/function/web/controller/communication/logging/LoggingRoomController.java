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
import com.future.function.web.model.request.communication.logging.LogMessageWebRequest;
import com.future.function.web.model.request.communication.logging.LoggingRoomWebRequest;
import com.future.function.web.model.request.communication.logging.TopicWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.logging.LogMessageWebResponse;
import com.future.function.web.model.response.feature.communication.logging.LoggingRoomWebResponse;
import com.future.function.web.model.response.feature.communication.logging.TopicWebResponse;
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
  public PagingResponse<LoggingRoomWebResponse> getLoggingRoomsByMember(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT }) Session session,
    @RequestParam(required = false) String search,
    @RequestParam( defaultValue = "1" ) int page,
    @RequestParam( defaultValue = "10" ) int size
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
  public DataResponse<LoggingRoomWebResponse> getLoggingRoom(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId
  ) {
    return LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(
      loggingRoomService.getLoggingRoom(loggingRoomId)
    );
  }

  @GetMapping(value = "/{loggingRoomId}/topics",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<TopicWebResponse> getLoggingRoomTopic(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId,
    @RequestParam( defaultValue = "1" ) int page,
    @RequestParam( defaultValue = "10" ) int size
  ) {
    return LoggingRoomResponseMapper.toPagingTopicResponse(
      topicService.getTopicByLoggingRoom(
        loggingRoomId, PageHelper.toPageable(page, size))
    );
  }

  @GetMapping(value = "/{loggingRoomId}/topics/{topicId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<TopicWebResponse> getTopic(
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
  public PagingResponse<LogMessageWebResponse> getLogMessages(
    @WithAnyRole(roles = { Role.ADMIN, Role.MENTOR, Role.STUDENT} ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId,
    @RequestParam( defaultValue = "1" ) int page,
    @RequestParam( defaultValue = "10" ) int size
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
    @RequestBody LogMessageWebRequest logMessageWebRequest
    ) {

    logMessageService.createLogMessage(
      logMessageRequestMapper.toLogMessage(
        logMessageWebRequest, session.getUserId(), topicId)
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
    @RequestBody TopicWebRequest topicWebRequest
  ) {
    topicService.createTopic(
      topicRequestMapper.toTopic(topicWebRequest, loggingRoomId, null)
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
    @RequestBody LoggingRoomWebRequest loggingRoomWebRequest
  ) {
    loggingRoomService.createLoggingRoom(
      loggingRoomRequestMapper.toLoggingRoom(loggingRoomWebRequest, null)
    );
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }

  @PutMapping(value = "/{loggingRoomId}/topics/{topicId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<TopicWebResponse> updateTopic(
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId,
    @PathVariable String topicId,
    @RequestBody TopicWebRequest topicWebRequest
  ) {
    return LoggingRoomResponseMapper.toDataResponseTopicResponse(
      topicService.updateTopic(
        topicRequestMapper.toTopic(topicWebRequest, loggingRoomId, topicId)
      )
    );
  }

  @PutMapping(value = "/{loggingRoomId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<LoggingRoomWebResponse> updateLoggingRoom(
    @WithAnyRole(roles = Role.MENTOR ) Session session,
    @PathVariable String loggingRoomId,
    @RequestBody LoggingRoomWebRequest loggingRoomWebRequest
  ) {
    return LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(
      loggingRoomService.updateLoggingRoom(
        loggingRoomRequestMapper.toLoggingRoom(loggingRoomWebRequest, loggingRoomId)
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
