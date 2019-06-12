package com.future.function.session.resolver;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    
    return Optional.ofNullable(webRequest)
      .map(this::getCookies)
      .map(this::getSession)
      .filter(session -> this.isUserOfValidRole(session, parameter))
      .orElseThrow(() -> new UnauthorizedException("Invalid Role"));
  }
  
  private boolean isUserOfValidRole(
    Session session, MethodParameter parameter
  ) {
    
    WithAnyRole parameterAnnotation;
    
    if (isClassAnnotated(parameter)) {
      parameterAnnotation = parameter.getContainingClass()
        .getAnnotation(WithAnyRole.class);
    } else if (isMethodAnnotated(parameter)) {
      parameterAnnotation = parameter.getMethodAnnotation(WithAnyRole.class);
    } else if (isParameterAnnotated(parameter)) {
      parameterAnnotation = parameter.getParameterAnnotation(WithAnyRole.class);
    } else {
      return false;
    }
    
    Role[] roles = parameterAnnotation.roles();
    
    if (roles.length == 0) {
      return true;
    }
    
    return Stream.of(roles)
      .anyMatch(role -> role.equals(session.getRole()));
  }
  
  private Session getSession(Cookie[] cookies) {
    
    return this.getCustomCookieValue(cookies)
      .map(valueOperations::get)
      .orElseThrow(
        () -> new UnauthorizedException("Invalid Session From Resolver"));
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
  
  private Cookie[] getCookies(NativeWebRequest webRequest) {
    
    return Optional.ofNullable(webRequest)
      .map(NativeWebRequest::getNativeRequest)
      .map(HttpServletRequest.class::cast)
      .map(HttpServletRequest::getCookies)
      .orElseGet(() -> new Cookie[0]);
  }
  
}
