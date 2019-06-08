package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionResponseSummaryRepository extends MongoRepository<QuestionResponseSummary, String> {

  /**
   * Find all Question summary from questionnaire
   *
   * @param questionnaire
   *
   * @return {@code List<QuestionsResponseSummary>} - Questions Response Summary list from database
   */
  List<QuestionResponseSummary> findAllByQuestionnaire(Questionnaire questionnaire);
}
