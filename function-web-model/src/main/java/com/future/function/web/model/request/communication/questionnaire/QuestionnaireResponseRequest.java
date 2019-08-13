package com.future.function.web.model.request.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireResponseRequest {

  @NotEmpty(message = "NotEmpty")
  private List<QuestionResponseRequest> responses;

}
