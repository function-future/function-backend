package com.future.function.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
  
  @Bean
  public Docket docket() {

//    return new Docket(DocumentationType.SWAGGER_2).select()
//      .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//      .paths(PathSelectors.any())
//      .build();
      return null;
  }
  
}
