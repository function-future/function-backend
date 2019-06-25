package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQuestionnaireSummaryRepository extends MongoRepository<UserQuestionnaireSummary, String> {

  /**
   * Find all user questionnaire summary
   * @param pageable pageable object for paging
   * @return {@code - Page<UserQuestionnaireSummary} - paged user questionnaire summary list from database
   */

  Page<UserQuestionnaireSummary> findAll (Pageable pageable);

  /**
   * Find specific user questionnaire summary by appraisee
   *
   * @param appraisee appraisee of user questionnaire summary to be search
   *
   * @return {@code - Optional<UserQuestionnaireSummary>} - user questionnaire summary from data base
   */
  Optional<UserQuestionnaireSummary> findFirstByAppraisee (User appraisee);
}
