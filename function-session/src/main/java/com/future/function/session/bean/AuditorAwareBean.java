package com.future.function.session.bean;

import com.future.function.common.exception.UnauthorizedException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "auditorAware")
public class AuditorAwareBean implements AuditorAware<String> {
  
  @Override
  public String getCurrentAuditor() {
    
    return Optional.ofNullable(SecurityContextHolder.getContext())
      .map(SecurityContext::getAuthentication)
      .map(Authentication::getName)
      .filter(name -> !name.equals("anonymousUser"))
      .orElseThrow(
        () -> new UnauthorizedException("Invalid Authentication From Auditor"));
  }
  
}
