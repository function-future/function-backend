package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface UserQuestionnaireSummaryService {

  Page<UserQuestionnaireSummary> getAllUsersQuestionnaireSumary(Pageable pageable);

  UserQuestionnaireSummary getUserQuestionnaireSummaryByAppraise(User Appraisee);

  UserQuestionnaireSummary getUserQuestionnaireSummary(String userQuestionnaireSummaryId);

  UserQuestionnaireSummary createUserQuestionnaireSummary(UserQuestionnaireSummary userQuestionnaireSummary);

  UserQuestionnaireSummary updateUserQuestionnaireSummary(UserQuestionnaireSummary userQuestionnaireSummary);

  void deleteUserQuestionnaireSummary(String userQuestionnaireSummaryId);
}
