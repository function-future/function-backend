package com.future.function.web.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

  @Value("${executor.thread.pool}")
  private int threadPool = 5;

  @Bean
  public ExecutorService executorService() {
    return Executors.newFixedThreadPool(threadPool);
  }

}
