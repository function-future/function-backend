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

@Service
public class QuestionnaireResultServiceImpl implements QuestionnaireResultService {


  private final UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  private final UserQuestionnaireSummaryRepositoryCustom userQuestionnaireSummaryRepositoryCustom;

  @Autowired
  public QuestionnaireResultServiceImpl(UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository, UserQuestionnaireSummaryRepositoryCustom userQuestionnaireSummaryRepositoryCustom) {
    this.userQuestionnaireSummaryRepository = userQuestionnaireSummaryRepository;
    this.userQuestionnaireSummaryRepositoryCustom = userQuestionnaireSummaryRepositoryCustom;
  }

  @Override
  public Page<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummaryByBatch(Batch batch, Pageable pageable) {
    return userQuestionnaireSummaryRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT, batch, pageable);
  }

  @Override
  public List<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummary(Batch batch, String search, Pageable pageable) {
    List<UserQuestionnaireSummary> results = userQuestionnaireSummaryRepositoryCustom.findAllByUserName(search);

    List<UserQuestionnaireSummary> ret = new ArrayList<>();

    for (UserQuestionnaireSummary result : results) {
      if(result.getBatch().getCode() == batch.getCode()) {
        ret.add(result);
      }
    }

    return ret;
  }
}
