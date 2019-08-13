package com.future.function.web.mapper.request.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.UserWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserRequestMapper {
  
  private final RequestValidator validator;
  
  @Autowired
  private UserRequestMapper(RequestValidator validator) {
    
    this.validator = validator;
  }
  
  public User toUser(UserWebRequest request) {
    
    return toValidatedUser(null, request);
  }
  
  private User toValidatedUser(String userId, UserWebRequest request) {
    
    request.setId(userId);
    
    validator.validate(request);
    
    User user = User.builder()
      .role(Role.toRole(request.getRole()))
      .email(request.getEmail()
               .toLowerCase())
      .name(request.getName())
      .phone(request.getPhone())
      .address(request.getAddress())
      .pictureV2(this.getFileV2(request))
      .batch(toBatch(request))
      .university(getUniversity(request))
      .build();
    
    if (userId != null) {
      user.setId(userId);
    }
    
    return user;
  }
  
  private FileV2 getFileV2(UserWebRequest request) {
    
    return Optional.of(request)
      .map(UserWebRequest::getAvatar)
      .filter(avatarSingleList -> !avatarSingleList.isEmpty())
      .map(list -> list.get(0))
      .map(this::buildFileV2)
      .orElse(null);
  }
  
  private FileV2 buildFileV2(String fileId) {
    
    return FileV2.builder()
      .id(fileId)
      .build();
  }
  
  private String getUniversity(UserWebRequest request) {
    
    return Optional.of(request)
      .map(UserWebRequest::getUniversity)
      .orElse(null);
  }
  
  private Batch toBatch(UserWebRequest request) {
    
    return Optional.of(request)
      .map(UserWebRequest::getBatch)
      .map(batchNumber -> Batch.builder()
        .code(batchNumber)
        .build())
      .orElse(null);
  }
  
  public User toUser(String userId, UserWebRequest request) {
    
    return toValidatedUser(userId, request);
  }
  
}
