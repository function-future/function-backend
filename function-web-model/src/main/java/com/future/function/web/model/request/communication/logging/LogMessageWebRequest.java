package com.future.function.web.model.request.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageWebRequest {

  @NotBlank(message = "NotBlank")
  private String text;

}
