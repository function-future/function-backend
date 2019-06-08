package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireResponseRepository extends MongoRepository<QuestionnaireResponse,String> {

  /**
   * Find all questionnaire response by questionnaire
   *
   * @param questionnaire
   *
   * @return {@code List<QuestionnaireResponse>} - questionnaire response list from database
   */
  List<QuestionnaireResponse> findByQuestionnaire(Questionnaire questionnaire);
}
