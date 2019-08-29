package com.future.function.web.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

  @Bean
  public ExecutorService executorService() {
    return Executors.newFixedThreadPool(5);
  }

}
