package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRespository
        extends MongoRepository<Question, String> {

  /**
   * Find all question by questionnaire
   * @param questionnaire
   * @return {@code List<Question>} - question list from data base
   */
  List<Question> findByQuestionnaire(Questionnaire questionnaire);
}
