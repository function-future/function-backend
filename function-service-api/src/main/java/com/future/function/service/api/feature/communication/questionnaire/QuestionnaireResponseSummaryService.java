package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionnaireResponseSummaryService {

  Page<QuestionnaireResponseSummary> getQuestionnairesSummariesBasedOnAppraisee(User appraisee, Pageable pageable);

  QuestionnaireResponseSummary getQuestionnaireResponseSummaryById(String questionnaireResponseSummaryId);

  List<QuestionResponseSummary> getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(String questionnaireResponseSummaryId,User Appraisee);

  QuestionResponseSummary getQuestionResponseSummaryById(String questionResponseSummaryId);

  QuestionResponse getQuestionResponseByQuestionResponseSummaryId(String questionResponseSummaryId);
}
