package com.future.function.web.model.response.feature.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerDetailResponse {

  private QuestionnaireSummaryDescriptionResponse questionnaireSummary;

  private QuestionQuestionnaireSummaryResponse questionSummary;

  private List<QuestionAnswerResponse> QuestionResponse;
}
