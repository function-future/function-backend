package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LoggingRoomWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoggingRoomRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public LoggingRoomRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public LoggingRoom toLoggingRoom(LoggingRoomWebRequest loggingRoomWebRequest, String loggingRoomId){
    return toValidatedLoggingRoom(loggingRoomWebRequest, loggingRoomId);
  }

  public LoggingRoom toValidatedLoggingRoom(LoggingRoomWebRequest loggingRoomWebRequest, String loggingRoomId){
    this.validator.validate(loggingRoomWebRequest);
    LoggingRoom loggingRoom = LoggingRoom.builder()
            .title(loggingRoomWebRequest.getTitle())
            .description(loggingRoomWebRequest.getDescription())
            .members(toListUserHelper(loggingRoomWebRequest.getMembers()))
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
