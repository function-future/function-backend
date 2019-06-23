package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseSummaryRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class QuestionnaireResponseSummaryServiceImpl implements QuestionnaireResponseSummaryService {

  private final QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository;

  private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

  private final QuestionnaireRepository questionnaireRepository;

  private final QuestionResponseRepository questionResponseRepository;

  @Autowired
  public QuestionnaireResponseSummaryServiceImpl(QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository, QuestionResponseSummaryRepository questionResponseSummaryRepository, QuestionnaireRepository questionnaireRepository, QuestionResponseRepository questionResponseRepository) {
    this.questionnaireResponseSummaryRepository = questionnaireResponseSummaryRepository;
    this.questionResponseSummaryRepository = questionResponseSummaryRepository;
    this.questionnaireRepository = questionnaireRepository;
    this.questionResponseRepository = questionResponseRepository;
  }


  @Override
  public Page<QuestionnaireResponseSummary> getQuestionnairesSummariesBasedOnAppraisee(User appraisee, Pageable pageable) {
    return questionnaireResponseSummaryRepository.findAllByAppraiseeAndDeletedFalse(appraisee, pageable);
  }

  @Override
  public QuestionnaireResponseSummary getQuestionnaireResponseSummaryById(String questionnaireResponseSummaryId) {
    return Optional.of(questionnaireResponseSummaryId)
            .map(questionnaireResponseSummaryRepository::findOne)
            .orElse(null);
  }

  @Override
  public List<QuestionResponseSummary> getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(String questionnaireResponseSummaryId, User appraisee) {
    Questionnaire questionnaire = questionnaireRepository.findOne(questionnaireResponseSummaryId);
    return questionResponseSummaryRepository.findAllByQuestionnaireAndAppraiseeAndDeletedFalse(questionnaire, appraisee);
  }

  @Override
  public QuestionResponseSummary getQuestionResponseSummaryById(String questionResponseSummaryId) {
    return Optional.of(questionResponseSummaryId)
            .map(questionResponseSummaryRepository::findOne)
            .orElse(null);
  }

  @Override
  public QuestionResponse getQuestionResponseByQuestionResponseSummaryId(String questionResponseSummaryId) {
    return Optional.of(questionResponseSummaryId)
            .map(questionResponseRepository::findOne)
            .orElse(null);
  }
}
