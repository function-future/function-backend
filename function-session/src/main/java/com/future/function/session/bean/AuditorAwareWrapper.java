package com.future.function.session.bean;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component(value = "auditorAware")
public class AuditorAwareWrapper implements AuditorAware<String> {

  @Override
  public String getCurrentAuditor() {

  try {
    return SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();
  } catch (NullPointerException e) {
    return "system";
  }
  }

}
