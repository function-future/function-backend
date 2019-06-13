package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.core.User;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionResponseService {

  List<QuestionResponse> getAllQuestionResponseByQuestionAndAppraisee(QuestionQuestionnaire questionQuestionnaire, User appraisee);

  QuestionResponse getQuestionResponseService(String questionReponseId);

  QuestionResponse createQuestionResponseService(QuestionResponse questionReponse);

  QuestionResponse updateQuestionResponseService(QuestionResponse questionReponse);

  void deleteQuestionResponseService(String questionReponseId);

}
