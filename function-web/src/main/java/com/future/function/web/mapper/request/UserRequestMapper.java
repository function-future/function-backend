package com.future.function.web.mapper.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.Role;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.user.User;
import com.future.function.web.model.request.user.UserWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class UserRequestMapper {
  
  private ObjectMapper objectMapper;
  
  private ObjectValidator validator;
  
  @Autowired
  private UserRequestMapper(
    ObjectMapper objectMapper, ObjectValidator validator
  ) {
    
    this.objectMapper = objectMapper;
    this.validator = validator;
  }
  
  public User toUser(String data) {
    
    UserWebRequest request = toUserWebRequest(data);
    
    return toUser(request.getEmail(), data);
  }
  
  public User toUser(String email, String data) {
    
    UserWebRequest request = toUserWebRequest(data);
    
    return toUser(email, request);
  }
  
  private User toUser(String email, UserWebRequest request) {
    
    User user = User.builder()
      .role(Role.toRole(request.getRole()))
      .email(email)
      .name(request.getName())
      .phone(request.getPhone())
      .address(request.getAddress())
      .batch(toBatch(request))
      .university(toUniversity(request))
      .build();
    
    return validator.validate(user);
  }
  
  private String toUniversity(UserWebRequest request) {
    
    return Optional.of(request)
      .map(UserWebRequest::getUniversity)
      .orElse(null);
  }
  
  private Batch toBatch(UserWebRequest request) {
    
    return Optional.of(request)
      .map(UserWebRequest::getBatch)
      .map(batchNumber -> Batch.builder()
        .number(batchNumber)
        .build())
      .orElse(null);
  }
  
  private UserWebRequest toUserWebRequest(String data) {
    
    UserWebRequest request;
    try {
      request = objectMapper.readValue(data, UserWebRequest.class);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: '{}'", e);
      throw new BadRequestException("Bad Request");
    }
    return request;
  }
  
}
