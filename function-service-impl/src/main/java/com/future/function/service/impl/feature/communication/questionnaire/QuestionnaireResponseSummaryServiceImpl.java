package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.QuestionQuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseSummaryRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionnaireResponseSummaryServiceImpl
  implements QuestionnaireResponseSummaryService {

  private final QuestionnaireResponseSummaryRepository
    questionnaireResponseSummaryRepository;

  private final QuestionResponseSummaryRepository
    questionResponseSummaryRepository;

  private final QuestionResponseRepository questionResponseRepository;

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;

  private final UserService userService;

  @Autowired
  public QuestionnaireResponseSummaryServiceImpl(
    QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository,
    QuestionResponseSummaryRepository questionResponseSummaryRepository,
    QuestionnaireRepository questionnaireRepository,
    QuestionResponseRepository questionResponseRepository,
    QuestionQuestionnaireRepository questionQuestionnaireRepository,
    UserService userService
  ) {

    this.questionnaireResponseSummaryRepository =
      questionnaireResponseSummaryRepository;
    this.questionResponseSummaryRepository = questionResponseSummaryRepository;
    this.questionResponseRepository = questionResponseRepository;
    this.questionQuestionnaireRepository = questionQuestionnaireRepository;
    this.userService = userService;
  }


  @Override
  public Page<QuestionnaireResponseSummary> getQuestionnairesSummariesBasedOnAppraisee(
    User appraisee, Pageable pageable
  ) {

    return questionnaireResponseSummaryRepository.findAllByAppraiseeAndDeletedFalse(
      appraisee, pageable);
  }

  @Override
  public QuestionnaireResponseSummary getQuestionnaireResponseSummaryById(
    String questionnaireResponseSummaryId
  ) {

    return Optional.of(questionnaireResponseSummaryId)
      .map(questionnaireResponseSummaryRepository::findOne)
      .orElse(null);
  }

  @Override
  public List<QuestionResponseSummary> getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(
    String questionnaireResponseSummaryId, User appraisee
  ) {

    QuestionnaireResponseSummary questionnaireResponseSummary =
      questionnaireResponseSummaryRepository.findOne(
        questionnaireResponseSummaryId);
    return questionResponseSummaryRepository.findAllByQuestionnaireAndAppraiseeAndDeletedFalse(
      questionnaireResponseSummary.getQuestionnaire(), appraisee);
  }

  @Override
  public QuestionResponseSummary getQuestionResponseSummaryById(
    String questionResponseSummaryId
  ) {

    return Optional.of(questionResponseSummaryId)
      .map(questionResponseSummaryRepository::findOne)
      .orElse(null);
  }

  @Override
  public List<QuestionResponse> getQuestionResponseByQuestionResponseSummaryId(
    String questionResponseSummaryId
  ) {

    return Optional.of(questionResponseSummaryId)
      .map(questionResponseSummaryRepository::findOne)
      .map(
        ignored -> questionResponseRepository.findAllByQuestionQuestionnaireAndAppraiseeAndDeletedFalse(
          questionQuestionnaireRepository.findOne(ignored.getQuestion()
                                                    .getId()),
          userService.getUser(ignored.getAppraisee()
                                .getId())
        ))
      .orElseGet(Collections::emptyList);
  }

}
