package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/core/user")
@WithAnyRole(roles = {
  Role.STUDENT, Role.MENTOR, Role.JUDGE, Role.ADMIN
})
public class UserDetailController {
  
  private final UserService userService;
  
  @Autowired
  public UserDetailController(UserService userService) {
    
    this.userService = userService;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/profile")
  public DataResponse<UserWebResponse> getProfile(
    Session session
  ) {
    
    return UserResponseMapper.toUserDataResponse(
      userService.getUserByEmail(session.getEmail()));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/password")
  public BaseResponse changePassword(
    Session session,
    @RequestBody
      ChangePasswordWebRequest request
  ) {
    
    userService.changeUserPassword(
      session.getEmail(), request.getNewPassword());
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
