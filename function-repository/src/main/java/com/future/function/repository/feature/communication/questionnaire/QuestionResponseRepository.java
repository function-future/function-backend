package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionResponseRepository extends MongoRepository<QuestionResponse, String> {

  /**
   * Find all question Response by question
   * @param question
   * @return {@code List<QuestionResponse>} - question list from data base
   */
  List<QuestionResponse> findAllByQuestionQuestionnaireAndAppraiseeAndDeletedFalse(QuestionQuestionnaire question, User apraisee);

}
