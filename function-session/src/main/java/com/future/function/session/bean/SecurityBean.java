package com.future.function.session.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityBean {

  @Bean
  public BCryptPasswordEncoder encoder() {

    return new BCryptPasswordEncoder();
  }

}
