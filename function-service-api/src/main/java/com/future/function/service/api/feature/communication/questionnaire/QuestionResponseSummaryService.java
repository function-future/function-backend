package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.User;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionResponseSummaryService {

  List<QuestionResponseSummary> getAllQuestionResponseSummaryByQuestionnaireAndAppraisee(
      Questionnaire questionnaire,
      User appraisee
    );

  QuestionResponseSummary getAllQuestionResponseSummaryByAppraiseeAndQuestionQuestionnaire(
    User appraisee,
    QuestionQuestionnaire questionQuestionnaire
  );

  QuestionResponseSummary getQuestionResponseSummary(String questionResponseSummaryId);

  QuestionResponseSummary createQuestionResponseSummary(QuestionResponseSummary questionResponseSummary);

  QuestionResponseSummary updateQuestionResponseSummary(QuestionResponseSummary questionResponseSummary);

  void deleteQuestionResponseSummary(QuestionResponseSummary questionResponseSummaryId);
}
