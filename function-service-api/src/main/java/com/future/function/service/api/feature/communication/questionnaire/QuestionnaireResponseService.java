package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponse;
import com.future.function.model.entity.feature.core.User;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionnaireResponseService {

  List<QuestionnaireResponse> getAllQuestionnaireResponseByQuestionnaireAndAppraisee(
      Questionnaire questionnaire,
      User appraisee
    );

  QuestionnaireResponse getQuestionnaireResponse(String questionnaireResponseId);

  QuestionnaireResponse createQuestionnaireResponse(QuestionnaireResponse questionnaireResponse);

  QuestionnaireResponse updateQuestionnaireResponse(QuestionnaireResponse questionnaireResponse);

  void deleteQuestionnaireResponse(String questionnaireResponseId);
}
