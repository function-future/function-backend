package com.future.function.web.model.request.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQuestionnaireRequest {

  @NotBlank(message = "Not Blank")
  private String description;

}
