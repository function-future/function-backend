package com.future.function.web.mapper.request.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.UserWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper class for incoming request for user feature.
 */
@Component
public class UserRequestMapper {
  
  private WebRequestMapper requestMapper;
  
  private RequestValidator validator;
  
  @Autowired
  private UserRequestMapper(
    WebRequestMapper requestMapper, RequestValidator validator
  ) {
  
    this.requestMapper = requestMapper;
    this.validator = validator;
  }
  
  /**
   * Converts JSON data to {@code User} object.
   *
   * @param data JSON data (in form of String) to be converted.
   *
   * @return {@code User} - Converted user object.
   */
  public User toUser(String data) {
  
    UserWebRequest request = requestMapper.toWebRequestObject(data,
                                                              UserWebRequest.class
    );
    
    return toValidatedUser(request.getEmail(), request);
  }
  
  private User toValidatedUser(String email, UserWebRequest request) {
  
    validator.validate(request);
  
    return User.builder()
      .role(Role.toRole(request.getRole()))
      .email(email)
      .name(request.getName())
      .password(getDefaultPassword(request.getName()))
      .phone(request.getPhone())
      .address(request.getAddress())
      .picture(new File())
      .batch(toBatch(request))
      .university(getUniversity(request))
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
        .number(batchNumber)
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
   * Converts JSON data to {@code User} object. This method is used for
   * update user purposes.
   *
   * @param email Email of user to be updated.
   * @param data  JSON data (in form of String) to be converted.
   *
   * @return {@code User} - Converted user object.
   */
  public User toUser(String email, String data) {
  
    return toValidatedUser(email, requestMapper.toWebRequestObject(data,
                                                                   UserWebRequest.class
    ));
  }
  
}
