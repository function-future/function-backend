package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.communication.questionnaire.UserQuestionnaireSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.UserQuestionnaireSummaryRepositoryCustom;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionnaireResultServiceImpl implements QuestionnaireResultService {


  private final UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  @Autowired
  public QuestionnaireResultServiceImpl(UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository) {
    this.userQuestionnaireSummaryRepository = userQuestionnaireSummaryRepository;
  }

  @Override
  public Page<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummaryByBatch(Batch batch, Pageable pageable) {
    return userQuestionnaireSummaryRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT, batch, pageable);
  }

  @Override
  public List<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummary(Batch batch, String search, Pageable pageable) {

    return userQuestionnaireSummaryRepository.findAllByUserName(search)
      .stream()
      .filter(summary  -> summary.getBatch().getCode() == batch.getCode())
      .collect(Collectors.toList());
  }
}
