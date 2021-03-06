package com.future.function.web.model.request.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicWebRequest {

  @Length(min = 1,
          max = 50,
          message = "Length")
  private String title;

}
