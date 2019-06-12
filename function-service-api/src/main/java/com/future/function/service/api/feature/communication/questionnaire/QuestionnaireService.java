package com.future.function.service.api.feature.communication.questionnaire;


import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionnaireService {

  Page<Questionnaire> getQuestionnaires(Pageable pageable);

  Page<Questionnaire> getQuestionnaireWithKeyword(String keyword, Pageable pageable);

  Page<Questionnaire> getQuestionnaireBelongToUser(String userId, Pageable pageable);

  Questionnaire getQuestionnaire(String questionnaireId);

  Questionnaire createQuestionnaire(Questionnaire questionnaire);

  Questionnaire updateQuestionnaire(Questionnaire questionnaire);

  void deleteQuestionnaire(String QuestionnaireId);

}
