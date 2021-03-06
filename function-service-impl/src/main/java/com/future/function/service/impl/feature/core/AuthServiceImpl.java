package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.communication.WsProperties;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.core.AuthService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;

  private final RedisTemplate<String, Session> redisTemplate;

  private final ValueOperations<String, Session> valueOperations;

  private final SessionProperties sessionProperties;

  private final WsProperties wsProperties;

  private final ChatroomService chatroomService;

  @Autowired
  public AuthServiceImpl(
          UserService userService, RedisTemplate<String, Session> redisTemplate,
          SessionProperties sessionProperties,
          WsProperties wsProperties, ChatroomService chatroomService) {

    this.userService = userService;
    this.redisTemplate = redisTemplate;
    this.valueOperations = redisTemplate.opsForValue();
    this.sessionProperties = sessionProperties;
    this.wsProperties = wsProperties;
    this.chatroomService = chatroomService;
  }

  @Override
  public User login(
    String email, String password, HttpServletResponse response
  ) {

    User user = userService.getUserByEmailAndPassword(email, password);

    Session session = Session.builder()
      .userId(user.getId())
      .batchId(this.getBatchId(user))
      .email(user.getEmail())
      .role(user.getRole())
      .build();

    valueOperations.set(session.getId(), session);

    this.setRedisExpirationAndCookie(response, session);

    this.setAuthenticationOnSecurityContextHolder(this.toAuthentication(user));

    return user;
  }

  private String getBatchId(User user) {

    return Optional.of(user)
      .map(User::getBatch)
      .map(Batch::getId)
      .orElse(null);
  }

  private void setAuthenticationOnSecurityContextHolder(
    Authentication authentication
  ) {

    SecurityContextHolder.getContext()
      .setAuthentication(authentication);
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }

  private UsernamePasswordAuthenticationToken toAuthentication(User user) {

    return new UsernamePasswordAuthenticationToken(
      user.getId(), user.getPassword());
  }

  @Override
  public void logout(String sessionId, HttpServletResponse response) {

    Optional.ofNullable(sessionId)
      .map(valueOperations::get)
      .ifPresent(session -> this.unsetRelatedDataToSession(session, response));
  }

  private void unsetRelatedDataToSession(
    Session session, HttpServletResponse response
  ) {

    redisTemplate.delete(session.getId());

    this.setCookie(response, null, 0);

    this.setAuthenticationOnSecurityContextHolder(null);
  }

  @Override
  public User getLoginStatus(
    String sessionId, HttpServletResponse response
  ) {

    return Optional.ofNullable(sessionId)
      .map(valueOperations::get)
      .map(session -> {
        this.setRedisExpirationAndCookie(response, session);
        return session;
      })
      .map(Session::getEmail)
      .map(userService::getUserByEmail)
      .orElseThrow(
        () -> new UnauthorizedException("Invalid Session From Service"));
  }

  private void setRedisExpirationAndCookie(
    HttpServletResponse response, Session session
  ) {

    redisTemplate.expire(
      session.getId(), sessionProperties.getExpireTime(), TimeUnit.SECONDS);

    this.setCookie(response, session.getId(), sessionProperties.getMaxAge());
  }

  private void setCookie(
    HttpServletResponse response, String sessionId, int maxAge
  ) {

    Cookie cookie = new Cookie(sessionProperties.getCookieName(), sessionId);
    cookie.setMaxAge(maxAge);
    cookie.setHttpOnly(true);
    cookie.setPath("/");

    response.addCookie(cookie);
  }

  @Override
  public void authorizeInboundChannel(String destination, String userId) {
    boolean isUriMatch = false;
    for (Map.Entry<String, String> entry: wsProperties.getTopic().entrySet()) {
      UriTemplate uriTemplate = new UriTemplate(entry.getValue());
      if (uriTemplate.matches(destination)) {
        isUriMatch = true;
        this.checkAuthorization(userId, entry.getKey(), uriTemplate.match(destination));
        break;
      }
    }
    if (!isUriMatch) {
      throw new ForbiddenException("Path Unknown");
    }
  }

  private void checkAuthorization(String userId, String key, Map<String, String> data) {
    switch (key) {
      case "chat":
        chatroomService.authorizeSubscription(userId, data.get("chatroomId"));
        break;
      case "chatroom":
      case "notification":
        if (data.get("userId").equals(userId)) {
          break;
        } else {
          throw new ForbiddenException("UserId didn't match");
        }
      default:
        throw new ForbiddenException("Forbidden");
    }
  }

}
