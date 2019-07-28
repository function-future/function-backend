package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LoggingRoomRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author : Ricky Kennedy
 * Created At : 11:11 28/07/2019
 */
@Slf4j
@Component
public class LoggingRoomRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public LoggingRoomRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public LoggingRoom toLoggingRoom(LoggingRoomRequest loggingRoomRequest, String loggingRoomId){
    return toValidatedLoggingRoom(loggingRoomRequest, loggingRoomId);
  }

  public LoggingRoom toValidatedLoggingRoom(LoggingRoomRequest loggingRoomRequest,  String loggingRoomId){
    LoggingRoom loggingRoom = LoggingRoom.builder()
            .title(loggingRoomRequest.getTitle())
            .description(loggingRoomRequest.getDescription())
            .members(toListUserHelper(loggingRoomRequest.getMembers()))
            .build();

    if(loggingRoomId != null) {
      loggingRoom.setId(loggingRoomId);
    }

    return loggingRoom;
  }

  public List<User> toListUserHelper(List<String> members) {
    return members.stream()
            .map(member -> User.builder().id(member).build())
            .collect(Collectors.toList());
  }
}
