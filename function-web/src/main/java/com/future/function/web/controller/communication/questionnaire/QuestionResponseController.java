package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionAnswerResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/communication/question-response")
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
  public DataResponse<List<QuestionAnswerResponse>> getQuestionnaireAnswerDetailSummary(
          @PathVariable String questionResponseSummaryId
  ) {
    return QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionAnswerDetailResponse(
            questionnaireResponseSummaryService.getQuestionResponseByQuestionResponseSummaryId(questionResponseSummaryId)
    );
  }
}
