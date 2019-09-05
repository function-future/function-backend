package com.future.function.session.resolver;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class SessionResolver implements HandlerMethodArgumentResolver {

  private final ValueOperations<String, Session> valueOperations;

  private final SessionProperties sessionProperties;

  @Autowired
  public SessionResolver(
    RedisTemplate<String, Session> redisTemplate,
    SessionProperties sessionProperties
  ) {

    this.valueOperations = redisTemplate.opsForValue();
    this.sessionProperties = sessionProperties;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {

    return this.isClassAnnotated(parameter) || this.isMethodAnnotated(
      parameter) || this.isParameterAnnotated(parameter);
  }

  private boolean isMethodAnnotated(MethodParameter parameter) {

    return Objects.nonNull(parameter.getMethodAnnotation(WithAnyRole.class));
  }

  private boolean isClassAnnotated(MethodParameter parameter) {

    return Objects.nonNull(parameter.getContainingClass()
                             .getAnnotation(WithAnyRole.class));
  }

  private boolean isParameterAnnotated(MethodParameter parameter) {

    return Objects.nonNull(parameter.getParameterAnnotation(WithAnyRole.class));
  }

  @Override
  public Object resolveArgument(
    MethodParameter parameter, ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest, WebDataBinderFactory binderFactory
  ) throws Exception {

    WithAnyRole parameterAnnotation = this.getParameterAnnotation(parameter);

    return Optional.ofNullable(webRequest)
      .map(this::getCookies)
      .map(cookies -> this.getSession(cookies, parameterAnnotation))
      .filter(session -> this.isUserOfValidRole(session, parameterAnnotation))
      .orElseThrow(() -> new ForbiddenException("Invalid Role"));
  }

  private boolean isUserOfValidRole(
    Session session, WithAnyRole parameterAnnotation
  ) {

    if (Objects.isNull(parameterAnnotation)) {
      return false;
    }

    if (parameterAnnotation.noUnauthorized()) {
      return true;
    }

    Role[] roles = parameterAnnotation.roles();

    if (roles.length == 0) {
      return true;
    }

    return Stream.of(roles)
      .anyMatch(role -> role.equals(session.getRole()));
  }

  private Session getSession(
    Cookie[] cookies, WithAnyRole parameterAnnotation
  ) {

    return this.getCustomCookieValue(cookies)
      .map(valueOperations::get)
      .map(this::reSetSecurityContextHolderAuthentication)
      .orElseGet(() -> this.returnEmptySessionOrException(parameterAnnotation));
  }

  private Session reSetSecurityContextHolderAuthentication(Session session) {

    UsernamePasswordAuthenticationToken authentication =
      new UsernamePasswordAuthenticationToken(session.getUserId(), "");
    SecurityContextHolder.getContext()
      .setAuthentication(authentication);
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

    return session;
  }

  private Session returnEmptySessionOrException(
    WithAnyRole parameterAnnotation
  ) {

    return Optional.ofNullable(parameterAnnotation)
      .filter(WithAnyRole::noUnauthorized)
      .map(ignored -> this.buildSessionWithUnknownRole())
      .orElseThrow(
        () -> new UnauthorizedException("Invalid Session From Resolver"));
  }

  private Session buildSessionWithUnknownRole() {

    return Session.builder()
      .role(Role.UNKNOWN)
      .build();
  }

  private Optional<String> getCustomCookieValue(Cookie[] cookies) {

    return Stream.of(cookies)
      .filter(this::isCookieNameMatch)
      .map(Cookie::getValue)
      .findFirst();
  }

  private boolean isCookieNameMatch(Cookie cookie) {

    return sessionProperties.getCookieName()
      .equals(cookie.getName());
  }

  private WithAnyRole getParameterAnnotation(MethodParameter parameter) {

    if (this.isClassAnnotated(parameter)) {
      return parameter.getContainingClass()
        .getAnnotation(WithAnyRole.class);
    } else if (this.isMethodAnnotated(parameter)) {
      return parameter.getMethodAnnotation(WithAnyRole.class);
    } else if (this.isParameterAnnotated(parameter)) {
      return parameter.getParameterAnnotation(WithAnyRole.class);
    } else {
      return null;
    }
  }

  private Cookie[] getCookies(NativeWebRequest webRequest) {

    return Optional.ofNullable(webRequest)
      .map(NativeWebRequest::getNativeRequest)
      .map(HttpServletRequest.class::cast)
      .map(HttpServletRequest::getCookies)
      .orElseGet(() -> new Cookie[0]);
  }

}
