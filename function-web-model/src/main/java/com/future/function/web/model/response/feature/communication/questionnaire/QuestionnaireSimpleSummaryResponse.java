package com.future.function.web.model.response.feature.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireSimpleSummaryResponse {

  String id;

  String title;

  String description;

  String status;

  long startDate;

  long dueDate;

  float score;

}
