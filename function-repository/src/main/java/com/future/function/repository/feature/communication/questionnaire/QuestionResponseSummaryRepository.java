package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionResponseSummaryRepository extends MongoRepository<QuestionResponseSummary, String> {

  /**
   * Find all question response summary from questionnaire
   *
   * @param questionnaire
   * @param appraisee
   *
   * @return {@code List<QuestionsResponseSummary>} - Questions Response Summary list from database
   */
  List<QuestionResponseSummary> findAllByQuestionnaireAndAppraisee(Questionnaire questionnaire, User appraisee);

  /**
   * Find all question response summary from appraisee and question
   *
   * @param appraisee
   * @param question
   * @param pageable
   *
   * @return {@code QuestionResponseSummary} - Questions response summary paged from database
   */
  QuestionResponseSummary findAllByAppraiseeAndQuestion(User appraisee, Question question, Pageable pageable);


}
