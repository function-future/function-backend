package com.future.function.web.model.response.feature.scoring;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuestionWebResponse {

  private int number;
  private String text;
  private List<OptionWebResponse> options;

}
