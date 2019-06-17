package com.future.function.web.model.request.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Author: PriagungSatyagama
 * Created At: 17:12 04/06/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

  @NotBlank(message = "NotBlank")
  private String message;

}
