package com.future.function.web.mapper.request.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.UserWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper class for incoming request for user feature.
 */
@Slf4j
@Component
public class UserRequestMapper {
  
  private final ObjectValidator validator;
  
  @Autowired
  private UserRequestMapper(
    WebRequestMapper requestMapper, ObjectValidator validator
  ) {
    
    this.validator = validator;
  }
  
  /**
   * Converts JSON data to {@code User} object.
   *
   * @param request JSON data (in form of String) to be converted.
   *
   * @return {@code User} - Converted user object.
   */
  public User toUser(UserWebRequest request) {
    
    return toValidatedUser(null, request);
  }
  
  private User toValidatedUser(String userId, UserWebRequest request) {
    
    validator.validate(request);
    
    User user = User.builder()
      .role(Role.toRole(request.getRole()))
      .email(request.getEmail()
               .toLowerCase())
      .name(request.getName())
      .password(getDefaultPassword(request.getName()))
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
      .map(list -> list.get(0))
      .map(this::buildFileV2)
      .orElseGet(FileV2::new);
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
  
  private String getDefaultPassword(String name) {
    
    return Optional.ofNullable(name)
      .map(String::toLowerCase)
      .map(n -> n.replace(" ", ""))
      .map(n -> n.concat("functionapp"))
      .orElse(null);
  }
  
  /**
   * Converts JSON request to {@code User} object. This method is used for
   * update user purposes.
   *
   * @param userId  Id of user to be updated.
   * @param request JSON request (in form of String) to be converted.
   *
   * @return {@code User} - Converted user object.
   */
  public User toUser(String userId, UserWebRequest request) {
    
    return toValidatedUser(userId, request);
  }
  
}
