package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface QuestionnaireParticipantRepository extends MongoRepository<QuestionnaireParticipant, String> {
  /**
   * Find all questionnaire by member and participant type
   *
   * @param member member of questionnaire participant to be search
   * @param participantType participant type of questionnaire participant to be search
   * @param pageable pageable object for paging
   *
   * @return {@code page<QuestionnaireParticipant>} - all questionnaire participant filtered by the query
   */

  Page<QuestionnaireParticipant> findAllByMemberAndParticipantType(User member, ParticipantType participantType, Pageable pageable);

  /**
   * Find all questionnaire participant by questionnaire
   *
   * @param questionnaire questionnaire of questionnaire participant to be searched
   * @param pageable pageable object for paging
   *
   * @return {@code List<QuestionnaireParticipant>} - all questionnaire participant filtered by the query
   */
  Page<QuestionnaireParticipant> findAllByQuestionnaire(Questionnaire questionnaire, Pageable pageable);
}
