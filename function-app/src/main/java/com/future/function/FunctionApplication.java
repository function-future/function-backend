package com.future.function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FunctionApplication {

  public static void main(String[] args) {

    SpringApplication.run(FunctionApplication.class, args);
  }

}
