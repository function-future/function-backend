package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSummaryDescriptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/communication/questionnaire-Response")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionnaireResponseController {


  private final QuestionnaireResponseSummaryService questionnaireResponseSummaryService;

  public QuestionnaireResponseController(QuestionnaireResponseSummaryService questionnaireResponseSummaryService) {
    this.questionnaireResponseSummaryService = questionnaireResponseSummaryService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping( value = "/{questionnaireResponseSummaryId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireSummaryDescriptionResponse>  getQuestionnaireSummaryDetail(
    @PathVariable String questionnaireResponseSummaryId
  ){

    return QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionnaireDataSummaryDescription(
      questionnaireResponseSummaryService.getQuestionnaireResponseSummaryById(questionnaireResponseSummaryId)
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping( value = "/{questionnaireResponseSummaryId}/questions",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<QuestionQuestionnaireSummaryResponse>> getQuestionSummaryResponse(
    @PathVariable String questionnaireResponseSummaryId
  ){

    return null;
  }
}
