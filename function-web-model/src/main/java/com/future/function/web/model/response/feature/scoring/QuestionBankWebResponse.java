package com.future.function.web.model.response.feature.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankWebResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;

  private String title;

  private String description;

}
