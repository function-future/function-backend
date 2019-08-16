package com.future.function.web.model.request.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireParticipantRequest {

  @NotBlank(message = "NotBlank")
  private String idParticipant;

}
