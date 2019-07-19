package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.AuthService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.AuthResponseMapper;
import com.future.function.web.model.request.core.AuthWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.AuthWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/core/auth")
public class AuthController {
  
  private final AuthService authService;
  
  @Autowired
  public AuthController(AuthService authService) {
    
    this.authService = authService;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PostMapping
  public DataResponse<AuthWebResponse> login(
    @RequestBody
      AuthWebRequest request, HttpServletResponse response
  ) {
    
    return AuthResponseMapper.toAuthDataResponse(authService.login(
      request.getEmail()
        .toLowerCase(), request.getPassword(), response));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public DataResponse<AuthWebResponse> getLoginStatus(
    @WithAnyRole
      Session session, HttpServletResponse servletResponse
  ) {
    
    return AuthResponseMapper.toAuthDataResponse(
      authService.getLoginStatus(session.getId(), servletResponse));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping
  public BaseResponse logout(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session, HttpServletResponse response
  ) {
    
    authService.logout(session.getId(), response);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
