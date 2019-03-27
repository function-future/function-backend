package com.future.function.web.mapper.request;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.user.User;
import com.future.function.model.util.constant.Role;
import com.future.function.web.model.request.user.UserWebRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserRequestMapper {

  private ObjectMapper objectMapper;

  @Autowired
  private UserRequestMapper(ObjectMapper objectMapper) {

    this.objectMapper = objectMapper;
  }

  public User toUser(String data) {

    UserWebRequest request;
    try {
      request = objectMapper.readValue(data, UserWebRequest.class);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: {}", e);
      throw new RuntimeException("Bad Request");
    }

    return User.builder()
        .role(Role.valueOf(request.getRole()))
        .email(request.getEmail())
        .name(request.getName())
        .phone(request.getPhone())
        .address(request.getAddress())
        .batch(Optional.of(request)
            .map(UserWebRequest::getBatch)
            .map(batchNumber -> Batch.builder()
                .number(batchNumber)
                .build())
            .orElse(null))
        .university(Optional.of(request)
            .map(UserWebRequest::getUniversity)
            .orElse(null))
        .build();
  }

}
