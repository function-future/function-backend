package com.future.function.web;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@TestConfiguration
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
