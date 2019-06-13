package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionQuestionnaireService {

  List<QuestionQuestionnaire> getQuestionQuestionnaireByQuestionnaire(String questionnaireId);

  QuestionQuestionnaire getQuestionQuestionnaire(String questionQuestionnaireId);

  QuestionQuestionnaire createQuestionQuestionnaire(QuestionQuestionnaire QuestionQuestionnaire);

  QuestionQuestionnaire updateQuestionQuestionnaire(QuestionQuestionnaire questionQuestionnaire);

  void deleteQuestionQuestionnaire(String QuestionQuestionnaireId);
}
