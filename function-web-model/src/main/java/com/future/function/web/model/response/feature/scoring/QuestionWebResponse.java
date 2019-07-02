package com.future.function.web.model.response.feature.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWebResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;
  private String label;
  private List<OptionWebResponse> options;
}
