package com.future.function;

import com.future.function.common.properties.core.FileProperties;
import com.future.function.common.properties.core.FunctionProperties;
import com.future.function.common.properties.core.SessionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableWebSecurity
@EnableRedisRepositories
@EnableScheduling
@EnableMongoRepositories
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
@EnableConfigurationProperties(value = {
  FileProperties.class, SessionProperties.class, FunctionProperties.class
})
public class FunctionApplication {

  public static void main(String[] args) {

    SpringApplication.run(FunctionApplication.class, args);
  }

}
