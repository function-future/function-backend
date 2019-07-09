package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSimpleSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSummaryDescriptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/communication/questionnaire-response")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionnaireResponseController {

  private final QuestionnaireResponseSummaryService questionnaireResponseSummaryService;

  private final UserService userService;

  public QuestionnaireResponseController(QuestionnaireResponseSummaryService questionnaireResponseSummaryService, UserService userService) {
    this.questionnaireResponseSummaryService = questionnaireResponseSummaryService;
    this.userService = userService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireSimpleSummaryResponse> getQuestionnairesSimpleSummary(
          @RequestParam String appraiseeId,
          @RequestParam(required = false, defaultValue = "1") int page,
          @RequestParam(required = false, defaultValue = "10") int size
  ){
    return QuestionnaireResponseMapper.toPagingQuestionnaireSimpleSummaryResponse(
            questionnaireResponseSummaryService.getQuestionnairesSummariesBasedOnAppraisee(
                    userService.getUser(appraiseeId), PageHelper.toPageable(page, size)),
            HttpStatus.OK
    );
  }


  @ResponseStatus(HttpStatus.OK)
  @GetMapping( value = "/{questionnaireResponseSummaryId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireSummaryDescriptionResponse> getQuestionnaireSummaryDetail(
    @PathVariable String questionnaireResponseSummaryId
  ){
    return QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionnaireDataSummaryDescription(
      questionnaireResponseSummaryService.getQuestionnaireResponseSummaryById(questionnaireResponseSummaryId)
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping( value = "/{questionnaireResponseSummaryId}/questions/{appraiseeId}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<QuestionQuestionnaireSummaryResponse>> getQuestionSummaryResponse(
    @PathVariable String questionnaireResponseSummaryId,
    @PathVariable String appraiseeId
  ){
    return QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionQuestionnaireSummaryResponseList(
      questionnaireResponseSummaryService.getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(questionnaireResponseSummaryId,
              userService.getUser(appraiseeId))
    );
  }
}
