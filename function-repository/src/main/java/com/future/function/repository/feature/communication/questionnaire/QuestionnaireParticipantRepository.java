package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireParticipantRepository extends MongoRepository<QuestionnaireParticipant, String> {
  Page<QuestionnaireParticipant> findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(User member, ParticipantType participantType, Pageable pageable);

  Page<QuestionnaireParticipant> findAllByQuestionnaireAndDeletedFalse(Questionnaire questionnaire, Pageable pageable);

  List<QuestionnaireParticipant> findAllByQuestionnaireAndDeletedFalse(Questionnaire questionnaire);

  Page<QuestionnaireParticipant> findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(Questionnaire questionnaire, ParticipantType participantType, Pageable pageable);

  Optional<QuestionnaireParticipant> findByQuestionnaireAndMemberAndParticipantTypeAndDeletedFalse(Questionnaire questionnaire,User member, ParticipantType participantType);


}
