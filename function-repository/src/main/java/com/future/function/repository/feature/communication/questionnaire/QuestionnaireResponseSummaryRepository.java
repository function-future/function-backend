package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireResponseSummaryRepository extends MongoRepository<QuestionnaireResponseSummary, String> {

  /**
   *
   * @param appraisee
   * @param pageable pageable object for paging
   * @return {@code Page<QuestionnaireResponseSummary>} - paged Questionnaire Response Summary -
   */
  Page<QuestionnaireResponseSummary> findAllByAppraisee(User appraisee, Pageable pageable);
}
