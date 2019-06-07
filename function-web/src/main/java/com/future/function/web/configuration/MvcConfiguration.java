package com.future.function.web.configuration;

import com.future.function.session.resolver.SessionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {
  
  private final SessionResolver sessionResolver;
  
  @Autowired
  public MvcConfiguration(SessionResolver sessionResolver) {
    
    this.sessionResolver = sessionResolver;
  }
  
  @Override
  public void addArgumentResolvers(
    List<HandlerMethodArgumentResolver> argumentResolvers
  ) {
    
    argumentResolvers.add(sessionResolver);
  }
  
}
