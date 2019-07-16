package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.User;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {
  
  User login(String email, String password, HttpServletResponse response);
  
  void logout(String sessionId, HttpServletResponse response);
  
  User getLoginStatus(
    String sessionId, HttpServletResponse servletResponse
  );
  
}
