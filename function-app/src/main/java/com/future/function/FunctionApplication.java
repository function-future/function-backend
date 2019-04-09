package com.future.function;

import com.future.function.common.properties.core.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableConfigurationProperties(FileProperties.class)
public class FunctionApplication {
  
  public static void main(String[] args) {
    
    SpringApplication.run(FunctionApplication.class, args);
  }
  
}
