package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResultService;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResultsResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.UserSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/communication/questionnaire-results")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionnaireResultsController {

  private final FileProperties fileProperties;

  private final QuestionnaireResultService questionnaireResultService;

  private final BatchService batchService;

  @Autowired
  public QuestionnaireResultsController(FileProperties fileProperties, QuestionnaireResultService questionnaireResultService, BatchService batchService) {
    this.fileProperties = fileProperties;
    this.questionnaireResultService = questionnaireResultService;
    this.batchService = batchService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<UserSummaryResponse> getUserSummary(
    @RequestParam String batchCode,
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int size
  ) {
      return QuestionnaireResultsResponseMapper.toPagingUserSummaryResponse(
        questionnaireResultService.getAppraisalsQuestionnaireSummaryByBatch(
          batchService.getBatchByCode(batchCode),
          PageHelper.toPageable(page, size)
        ),
        fileProperties.getUrlPrefix()
      );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value="/{batchCode}/user-summary-response/{userSummaryId}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserSummaryResponse> getUserSummaryById(
          @PathVariable String batchCode,
          @PathVariable String userSummaryId
  ) {
    return QuestionnaireResultsResponseMapper.toDataResponseUserSummaryResponse(
            questionnaireResultService.getAppraisalsQuestionnaireSummaryById(userSummaryId),
            fileProperties.getUrlPrefix()
    );
  }
}
