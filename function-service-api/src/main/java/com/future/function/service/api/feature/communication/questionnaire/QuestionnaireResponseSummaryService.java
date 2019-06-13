package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionnaireResponseSummaryService {

  Page<QuestionnaireResponseSummary> getAllQuesionnaireResponseSummaryByAppraisee(User appraisee, Pageable pageable);

  QuestionnaireResponseSummary getQuestionnaireResponseSummary(String questionnaireResponseSummaryId);

  QuestionnaireResponseSummary createQuestionnaireResponseSummary(QuestionnaireResponseSummary questionnaireResponseSummary);

  QuestionnaireResponseSummary updateQuestionnaireResponseSummary(QuestionnaireResponseSummary questionnaireResponseSummary);

  void deleteQuestionnaireResponseSummary(String questionnaireResponseSummaryId);

}
