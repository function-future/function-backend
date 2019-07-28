package com.future.function.web.model.request.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Author : Ricky Kennedy
 * Created At : 23:35 27/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageRequest {

  @NotBlank(message = "NotBlank")
  private String text;
}
