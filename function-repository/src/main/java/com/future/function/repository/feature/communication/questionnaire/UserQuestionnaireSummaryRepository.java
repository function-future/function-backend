package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserQuestionnaireSummaryRepository
  extends MongoRepository<UserQuestionnaireSummary, String>,
  UserQuestionnaireSummaryRepositoryCustom {

  Page<UserQuestionnaireSummary> findAllByDeletedFalse (Pageable pageable);

  Optional<UserQuestionnaireSummary> findFirstByAppraiseeAndDeletedFalse (User appraisee);

  Page<UserQuestionnaireSummary> findAllByRoleOrRoleAndBatchAndDeletedFalse (Role role1, Role role, Batch batch, Pageable pageable);

  Page<UserQuestionnaireSummary> findAllByRoleAndDeletedFalse (Role role, Pageable pageable);

//  @Query()
//  Page<UserQuestionnaireSummary> findAllByRoleAndBatchAndDeletedFalseAndMemberNameIgnoreCaseContaining(Role role, Batch batch, String keyword,Pageable pageable)

}
