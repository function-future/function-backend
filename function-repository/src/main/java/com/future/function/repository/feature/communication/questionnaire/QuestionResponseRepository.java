package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionResponseRepository extends MongoRepository<QuestionResponse, String> {

  /**
   * Find all question Response by question
   * @param question
   * @return {@code List<QuestionResponse>} - question list from data base
   */
  List<QuestionResponse> findAllByQuestionAndAppraisee(Question question, User apraisee);

}
