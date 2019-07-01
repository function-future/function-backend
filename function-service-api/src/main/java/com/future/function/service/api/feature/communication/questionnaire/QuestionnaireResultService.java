package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface QuestionnaireResultService {

  Page<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummaryByBatch(Batch batch, Pageable pageable);

  List<UserQuestionnaireSummary> getAppraisalsQuestionnaireSummary(Batch batch, String search, Pageable pageable);
}