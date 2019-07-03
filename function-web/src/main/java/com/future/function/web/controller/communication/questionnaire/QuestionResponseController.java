package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionAnswerDetailResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/communication/question-Response")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionResponseController {

  private final QuestionnaireResponseSummaryService questionnaireResponseSummaryService;

  public QuestionResponseController(QuestionnaireResponseSummaryService questionnaireResponseSummaryService) {
    this.questionnaireResponseSummaryService = questionnaireResponseSummaryService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionResponseSummaryId}")
  public DataResponse<QuestionQuestionnaireSummaryResponse> getQuestionQuestionnaireSummaryResponse(
          @PathVariable String questionResponseSummaryId
  ) {
    return QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionQuestionnaireSummaryResponse(
      questionnaireResponseSummaryService.getQuestionResponseSummaryById(questionResponseSummaryId)
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionResponseSummaryId}/responses")
  public DataResponse<QuestionAnswerDetailResponse> getQuestionnaireAnswerDetailSummary(
          @PathVariable String questionResponseSummaryId
  ) {
    QuestionResponseSummary questionResponseSummary =
            questionnaireResponseSummaryService.getQuestionResponseSummaryById(questionResponseSummaryId);
    return QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionAnswerDetailResponse(
            questionnaireResponseSummaryService.getQuestionnaireResponseSummaryById(questionResponseSummary.getQuestionnaire().getId()),
            questionnaireResponseSummaryService.getQuestionResponseSummaryById(questionResponseSummary.getId()),
            questionnaireResponseSummaryService.getQuestionResponseByQuestionResponseSummaryId(questionResponseSummaryId)
    );
  }
}
