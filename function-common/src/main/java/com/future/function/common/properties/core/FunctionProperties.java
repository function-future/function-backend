package com.future.function.common.properties.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("function")
public class FunctionProperties {

  private String mailGreetingMessage = "%s %s %s %s";

  private String uiUrl = "http://localhost:10001";
}
