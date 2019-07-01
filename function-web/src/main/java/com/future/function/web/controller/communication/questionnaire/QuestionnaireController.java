package com.future.function.web.controller.communication.questionnaire;


import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.questionnaire.QuestionnaireRequestMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/communication/questionnaires")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionnaireController {

  private final QuestionnaireService questionnaireService;

  private final UserService userService;

  private final QuestionnaireRequestMapper questionnaireRequestMapper;

  @Autowired
  public QuestionnaireController(QuestionnaireService questionnaireService, UserService userService, QuestionnaireRequestMapper questionnaireRequestMapper) {
    this.questionnaireService = questionnaireService;
    this.userService = userService;
    this.questionnaireRequestMapper = questionnaireRequestMapper;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireDetailResponse> getQuestionnaires(
          @RequestParam(required = false) String search,
          @RequestParam(required = false, defaultValue = "1") int page,
          @RequestParam(required = false, defaultValue = "10") int size,
          Session session
  ){
    if( search != null) {
      return QuestionnaireResponseMapper
              .toPagingQuestionnaireDetailResponse(
                      questionnaireService
                              .getQuestionnairesWithKeyword(search, PageHelper.toPageable(page, size)));
    } else {
      return QuestionnaireResponseMapper
              .toPagingQuestionnaireDetailResponse(
                      questionnaireService.getAllQuestionnaires(PageHelper.toPageable(page,size)));
    }
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireDetailResponse> createQuestionnaire(
    @RequestBody QuestionnaireRequest questionnaireRequest,
    Session session
  ) {
    Questionnaire newQuestionnaire = Questionnaire.builder()
      .title(questionnaireRequest.getTitle())
      .description(questionnaireRequest.getDesc())
      .startDate(questionnaireRequest.getStartDate())
      .dueDate(questionnaireRequest.getDueDate())
      .build();
    return QuestionnaireResponseMapper
      .toDataResponseQuestionnaireDetailResponse(
        questionnaireService.createQuestionnaire(
          newQuestionnaire,
          userService.getUser(session.getUserId())
        ),
        HttpStatus.CREATED
      );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value="/{questionnaireId:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireDetailResponse> getQuestionnaire(@PathVariable String questionnaireId){
    return QuestionnaireResponseMapper.toDataResponseQuestionnaireDetailResponse(questionnaireService.getQuestionnaire(questionnaireId), HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{questionnaireId}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public DataResponse<QuestionnaireDetailResponse> updateQuestionnaire(
    @PathVariable String questionnaireId,
    QuestionnaireRequest questionnaireRequest
  ) {
    return QuestionnaireResponseMapper.toDataResponseQuestionnaireDetailResponse(
      questionnaireService.updateQuestionnaire(
        questionnaireRequestMapper.toQuestionnaire(questionnaireRequest, questionnaireId)),
      HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value ="/{questionnaireId:.+}")
  public BaseResponse deleteQuestionnaire(@PathVariable String questionnaireId){
    questionnaireService.deleteQuestionnaire(questionnaireId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }


}
