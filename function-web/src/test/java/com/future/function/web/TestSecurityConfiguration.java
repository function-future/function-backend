package com.future.function.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class TestSecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  @Override
  protected void configure(
    HttpSecurity http
  ) throws Exception {
    
    http.formLogin()
      .disable()
      .csrf()
      .disable()
      .httpBasic()
      .disable()
      .authorizeRequests()
      .antMatchers("*/*")
      .permitAll();
  }
  
}
