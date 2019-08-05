package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionResponseSummaryRepository extends MongoRepository<QuestionResponseSummary, String> {

  /**
   * Find all question response summary from questionnaire
   *
   * @param questionnaire
   * @param appraisee
   *
   * @return {@code List<QuestionsResponseSummary>} - Questions Response Summary list from database
   */
  List<QuestionResponseSummary> findAllByQuestionnaireAndAppraiseeAndDeletedFalse(Questionnaire questionnaire, User appraisee);

  /**
   * Find all question response summary from appraisee and question
   *
   * @param appraisee
   * @param question
   *
   * @return {@code QuestionResponseSummary} - Questions response summary paged from database
   */
  Optional<QuestionResponseSummary> findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(User appraisee, QuestionQuestionnaire question);

}
