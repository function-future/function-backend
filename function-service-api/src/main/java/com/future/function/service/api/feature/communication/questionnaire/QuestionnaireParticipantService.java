package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionnaireParticipantService {

  Page<QuestionnaireParticipant> getAllQuestionnaireParticipantByMemberAndParticipantType(
      User member,
      ParticipantType participantType,
      Pageable pageable
    );

  List<QuestionnaireParticipant> getAllQuestionnaireParticipantByQuestionnaire(Questionnaire questionnaire);

  QuestionnaireParticipant getQuestionnaireParticipant(String questionnaireParticipantId);

  QuestionnaireParticipant createQuestionnaireParticipant(QuestionnaireParticipant questionnaireParticipant);

  QuestionnaireParticipant updateQuestionnaireParticipant(QuestionnaireParticipant questionnaireParticipant);

  void deleteQuestionnaireParticipant(QuestionnaireParticipant questionnaireParticipantId);

}
