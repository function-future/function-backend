package com.future.function.web.model.response.feature.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerResponse {

  private String name;

  private String avatar;

  private Float score;

  private String comment;
}
