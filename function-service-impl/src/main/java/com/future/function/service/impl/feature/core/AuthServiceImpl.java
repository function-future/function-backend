package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.AuthService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
  
  private final UserService userService;
  
  private final RedisTemplate<String, Session> redisTemplate;
  
  private final HashOperations<String, String, Session> hashOperations;
  
  private final SessionProperties sessionProperties;
  
  @Autowired
  public AuthServiceImpl(
    UserService userService, RedisTemplate<String, Session> redisTemplate,
    SessionProperties sessionProperties
  ) {
    
    this.userService = userService;
    this.redisTemplate = redisTemplate;
    this.hashOperations = redisTemplate.opsForHash();
    this.sessionProperties = sessionProperties;
  }
  
  @Override
  public User login(
    String email, String password, HttpServletResponse response
  ) {
    
    User user = userService.getUserByEmailAndPassword(email, password);
    
    Session session = Session.builder()
      .email(user.getEmail())
      .role(user.getRole())
      .build();
    
    hashOperations.put(session.getId(), sessionProperties.getKey(), session);
    redisTemplate.expire(
      session.getId(), sessionProperties.getExpireTime(), TimeUnit.SECONDS);
    this.setCookie(response, session.getId(), sessionProperties.getMaxAge());
    
    return user;
  }
  
  private void setCookie(
    HttpServletResponse response, String sessionId, int maxAge
  ) {
    
    Cookie cookie = new Cookie(sessionProperties.getCookieName(), sessionId);
    cookie.setMaxAge(maxAge);
    cookie.setHttpOnly(true);
    
    response.addCookie(cookie);
  }
  
  @Override
  public void logout(String sessionId, HttpServletResponse response) {
    
    Optional.ofNullable(sessionId)
      .map(id -> hashOperations.get(id, sessionProperties.getKey()))
      .ifPresent(session -> this.deleteAndUnsetCookie(session, response));
  }
  
  private void deleteAndUnsetCookie(
    Session session, HttpServletResponse response
  ) {
    
    hashOperations.delete(session.getId(), sessionProperties.getKey());
    
    this.setCookie(response, null, 0);
  }
  
  @Override
  public User getLoginStatus(String sessionId) {
    
    return Optional.ofNullable(sessionId)
      .map(id -> hashOperations.get(id, sessionProperties.getKey()))
      .map(Session::getEmail)
      .map(userService::getUserByEmail)
      .orElseThrow(
        () -> new UnauthorizedException("Invalid Session From Service"));
  }
  
}
