package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.questionnaire.MyQuestionnaireRequestMapper;
import com.future.function.web.mapper.response.communication.questionnaire.MyQuestionnaireResponseMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireResponseRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/communication/my-questionnaires")
@WithAnyRole(roles = { Role.STUDENT, Role.MENTOR })
public class MyQuestionnaireController {

  private final MyQuestionnaireService myQuestionnaireService;

  private final QuestionnaireService questionnaireService;

  private final UserService userService;

  private final MyQuestionnaireRequestMapper myQuestionnaireRequestMapper;

  private final FileProperties fileProperties;

  @Autowired
  public MyQuestionnaireController(
    MyQuestionnaireService myQuestionnaireService,
    QuestionnaireService questionnaireService, UserService userService,
    MyQuestionnaireRequestMapper myQuestionnaireRequestMapper,
    FileProperties fileProperties
  ) {

    this.myQuestionnaireService = myQuestionnaireService;
    this.questionnaireService = questionnaireService;
    this.userService = userService;
    this.myQuestionnaireRequestMapper = myQuestionnaireRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireDetailResponse> getMyQuestionnaires(
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size,
    @RequestParam(required = false)
      String search,
      Session session
  ) {

    return QuestionnaireResponseMapper.toPagingQuestionnaireDetailResponse(
      myQuestionnaireService.getQuestionnairesByMemberLoginAsAppraiser(
        userService.getUser(session.getUserId()),
        search,
        PageHelper.toPageable(page, size)
      ));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/appraisees",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<AppraiseeResponse>> getListAprraisees(
    @PathVariable
      String questionnaireId, Session session
  ) {

    return MyQuestionnaireResponseMapper.toDataResponseAppraiseeResponseList(
      myQuestionnaireService.getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(
        questionnaireService.getQuestionnaire(questionnaireId),
        userService.getUser(session.getUserId())
      ), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/appraisees-done",
    produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<QuestionnaireDoneResponse>> getListAprraiseesDone(
    @PathVariable
      String questionnaireId, Session session
  ) {
    return MyQuestionnaireResponseMapper.toDataResponseQuestionnaireDoneResponseList(
      myQuestionnaireService.getListAppraiseeDone(
        questionnaireService.getQuestionnaire(questionnaireId),
        userService.getUser(session.getUserId())
        ),fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/appraisees/{appraiseeId}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AppraisalDataResponse> getQuestionnaireData(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String appraiseeId
  ) {

    return MyQuestionnaireResponseMapper.toDataResponseQuestionnaireSummaryDescriptionResponse(
      questionnaireService.getQuestionnaire(questionnaireId),
      userService.getUser(appraiseeId), fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/appraisees/{appraiseeId}/questions",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<QuestionQuestionnaireResponse>> getQuestion(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String appraiseeId
  ) {

    return MyQuestionnaireResponseMapper.toDataResponseQuestionQuestionnaireResponseList(
      myQuestionnaireService.getQuestionsFromQuestionnaire(
        questionnaireService.getQuestionnaire(questionnaireId)));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/{questionnaireId}/appraisees/{appraiseeId}/questions",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse addQuestionnaireResponse(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String appraiseeId, Session session,
    @RequestBody
      QuestionnaireResponseRequest responses
  ) {

    myQuestionnaireService.createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
      questionnaireService.getQuestionnaire(questionnaireId),
      myQuestionnaireRequestMapper.toListQuestionResponseQueue(
        responses.getResponses(), userService.getUser(session.getUserId()),
        userService.getUser(appraiseeId)
      ), userService.getUser(session.getUserId()),
      userService.getUser(appraiseeId)
    );
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
