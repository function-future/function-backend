package com.future.function.web.mapper.request.core;

import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class UserDetailRequestMapper {
  
  private final RequestValidator validator;
  
  @Autowired
  public UserDetailRequestMapper(RequestValidator validator) {
    
    this.validator = validator;
  }
  
  public Pair<String, String> toOldAndNewPasswordPair(
    ChangePasswordWebRequest request
  ) {
    
    validator.validate(request);
    
    return Pair.of(request.getOldPassword(), request.getNewPassword());
  }
  
}
