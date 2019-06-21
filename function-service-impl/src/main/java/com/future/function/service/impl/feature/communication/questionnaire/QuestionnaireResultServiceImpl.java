package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.communication.questionnaire.UserQuestionnaireSummaryRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireResultServiceImpl implements QuestionnaireResultService {

  @Autowired
  UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  @Override
  public Page<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummary(Batch batch, Pageable pageable) {
    return userQuestionnaireSummaryRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT, batch, pageable);
  }

  @Override
  public Page<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummary(Batch batch, String search, Pageable pageable) {
    return null;
  }
}
