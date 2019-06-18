package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionnaireService {

  Page<Questionnaire> getAllQuestionnaires(Pageable pageable);

  Page<Questionnaire> getQuestionnairesWithKeyword(String keyword, Pageable pageable);

  Page<Questionnaire> getQuestionnairesBelongToAppraisee(String appraoseeId, Pageable pageable);

  Questionnaire getQuestionnaire(String questionnaireId);

  Questionnaire createQuestionnaire(Questionnaire questionnaire, User author);

  Questionnaire updateQuestionnaire(Questionnaire questionnaire);

  void deleteQuestionnaire(String QuestionnaireId);

  // Questionnaire Questions
  // '/api/communication.questionnaires/{questionnaireId}/questions
  List<QuestionQuestionnaire> getQuestionsByIdQuestionnaire(Questionnaire questionnaire);

  QuestionQuestionnaire createQuestionQuestionnaire(QuestionQuestionnaire questionQuestionnaire);

  QuestionQuestionnaire updateQuestionQuestionnaire(QuestionQuestionnaire questionQuestionnaire);

  void questionQuestionnaire(String QuestionquestionnaireId);

  //Questionnaire Appraiser

  Page<QuestionnaireParticipant> getQuestionnaireAppraiser(Questionnaire questionnaire,Pageable pageable);

  QuestionnaireParticipant addQuestionnaireAppraiserToQuestionnaire(Questionnaire questionnaire);

  void deleteQuestionnaireAppraiserFromQuestionnaire(String questionnaireId);

  //Questionnaire Appraisee

  Page<QuestionnaireParticipant> getQuestionnaireAppraisee(Questionnaire questionnaire, Pageable pageable);

  QuestionnaireParticipant addQuestionnaireAppraiseeToQuestionnaire(Questionnaire questionnaire);

  void deleteQuestionnaireAppraiseeFromQuestionnaire(String questionnaireId);

}
