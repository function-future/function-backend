package com.future.function;

import com.future.function.common.properties.core.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main class of the application.
 * <p>
 * This class contains the {@code main} function to be executed when running
 * the Spring Boot application. Annotations related to the application's
 * requirements are also included and declared here.
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories
@EnableConfigurationProperties(FileProperties.class)
public class FunctionApplication {

  /**
   * The {@code main} function to be executed.
   *
   * @param args Arguments passed in execution command. Not used in this
   *             application.
   */
  public static void main(String[] args) {

    SpringApplication.run(FunctionApplication.class, args);
  }

}
