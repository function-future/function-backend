package com.future.function.web.model.response.feature.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWebResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;
  private String text;
  private List<OptionWebResponse> options;
}
