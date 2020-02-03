package com.future.function.web.controller.communication.questionnaire;


import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.questionnaire.QuestionQuestionnaireRequestMapper;
import com.future.function.web.mapper.request.communication.questionnaire.QuestionnaireRequestMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionQuestionnaireResponseMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireParticipantResponseMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.model.request.communication.questionnaire.QuestionQuestionnaireRequest;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireParticipantRequest;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireParticipantDescriptionResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireParticipantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/communication/questionnaires")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionnaireController {

  private final QuestionnaireService questionnaireService;

  private final UserService userService;

  private final QuestionnaireRequestMapper questionnaireRequestMapper;

  private final QuestionQuestionnaireRequestMapper
    questionQuestionnaireRequestMapper;

  private final FileProperties fileProperties;

  @Autowired
  public QuestionnaireController(
    QuestionnaireService questionnaireService, UserService userService,
    QuestionnaireRequestMapper questionnaireRequestMapper,
    QuestionQuestionnaireRequestMapper questionQuestionnaireRequestMapper,
    FileProperties fileProperties
  ) {

    this.questionnaireService = questionnaireService;
    this.userService = userService;
    this.questionnaireRequestMapper = questionnaireRequestMapper;
    this.questionQuestionnaireRequestMapper =
      questionQuestionnaireRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireDetailResponse> getQuestionnaires(
    @RequestParam(required = false)
      String search,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    if (search != null) {
      return QuestionnaireResponseMapper.toPagingQuestionnaireDetailResponse(
        questionnaireService.getQuestionnairesWithKeyword(search,
                                                          PageHelper.toPageable(
                                                            page, size)
        ));
    } else {
      return QuestionnaireResponseMapper.toPagingQuestionnaireDetailResponse(
        questionnaireService.getAllQuestionnaires(
          PageHelper.toPageable(page, size)));
    }
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireDetailResponse> createQuestionnaire(
    @RequestBody
      QuestionnaireRequest questionnaireRequest, Session session
  ) {

  if(!questionnaireService.validateQuestionnaire(
      null,
      questionnaireRequest.getStartDate(),
      questionnaireRequest.getDueDate())
    ){
      return ResponseHelper.toDataResponse(
        HttpStatus.FORBIDDEN,
        QuestionnaireDetailResponse.builder().build()
      );
    }
    Questionnaire newQuestionnaire = Questionnaire.builder()
      .title(questionnaireRequest.getTitle())
      .description(questionnaireRequest.getDesc())
      .startDate(questionnaireRequest.getStartDate())
      .dueDate(questionnaireRequest.getDueDate())
      .build();
    return QuestionnaireResponseMapper.toDataResponseQuestionnaireDetailResponse(
      questionnaireService.createQuestionnaire(newQuestionnaire,
                                               userService.getUser(
                                                 session.getUserId())
      ), HttpStatus.CREATED);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireDetailResponse> getQuestionnaire(
    @PathVariable
      String questionnaireId
  ) {

    return QuestionnaireResponseMapper.toDataResponseQuestionnaireDetailResponse(
      questionnaireService.getQuestionnaire(questionnaireId), HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{questionnaireId}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireDetailResponse> updateQuestionnaire(
    @PathVariable
      String questionnaireId,
    @RequestBody
      QuestionnaireRequest questionnaireRequest
  ) {

    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaireRequest.getStartDate(),
      questionnaireRequest.getDueDate())
    ){
      return ResponseHelper.toDataResponse(
        HttpStatus.FORBIDDEN,
        QuestionnaireDetailResponse.builder().build()
      );
    }

    return QuestionnaireResponseMapper.toDataResponseQuestionnaireDetailResponse(
      questionnaireService.updateQuestionnaire(
        questionnaireRequestMapper.toQuestionnaire(questionnaireRequest,
                                                   questionnaireId
        )), HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{questionnaireId}")
  public BaseResponse deleteQuestionnaire(
    @PathVariable
      String questionnaireId
  ) {

    questionnaireService.deleteQuestionnaire(questionnaireId);

    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/questions",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<QuestionQuestionnaireResponse>> getQuestionsQuestionnaire(
    @PathVariable
      String questionnaireId
  ) {

    return QuestionQuestionnaireResponseMapper.toDataResponseListQuestionQuestionnaireResponse(
      questionnaireService.getQuestionsByIdQuestionnaire(questionnaireId),
      HttpStatus.OK
    );
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/{questionnaireId}/questions",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionQuestionnaireResponse> createQuestionQuestionnaire(
    @PathVariable
      String questionnaireId,
    @RequestBody
      QuestionQuestionnaireRequest questionQuestionnaireRequest
  ) {
    Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaire.getStartDate(),
      questionnaire.getDueDate())
    ){
      return ResponseHelper.toDataResponse(
        HttpStatus.FORBIDDEN,
        QuestionQuestionnaireResponse.builder().build()
      );
    }

    return QuestionQuestionnaireResponseMapper.toDataResponseQuestionQuestionnaireResponse(
      questionnaireService.createQuestionQuestionnaire(
        questionQuestionnaireRequestMapper.toQuestionQuestionnaire(
          questionQuestionnaireRequest, null,
          questionnaire
        )), HttpStatus.CREATED);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{questionnaireId}/questions/{questionId}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionQuestionnaireResponse> updateQuestionQuestionnaire(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String questionId,
    @RequestBody
      QuestionQuestionnaireRequest questionQuestionnaireRequest
  ) {
    Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaire.getStartDate(),
      questionnaire.getDueDate())
    ){
      return ResponseHelper.toDataResponse(
        HttpStatus.FORBIDDEN,
        QuestionQuestionnaireResponse.builder().build()
      );
    }

    return QuestionQuestionnaireResponseMapper.toDataResponseQuestionQuestionnaireResponse(
      questionnaireService.updateQuestionQuestionnaire(
        questionQuestionnaireRequestMapper.toQuestionQuestionnaire(
          questionQuestionnaireRequest, questionId,
          questionnaire
        )), HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{questionnaireId}/questions/{questionId}")
  public BaseResponse deleteQuestionQuestionnaire(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String questionId
  ) {

    questionnaireService.deleteQuestionQuestionnaire(questionId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/appraiser",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireParticipantDescriptionResponse> getAppraiserQuestionnaire(
    @PathVariable
      String questionnaireId,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    return QuestionnaireParticipantResponseMapper.toPagingParticipantDescriptionResponse(
      questionnaireService.getQuestionnaireAppraiser(
        questionnaireService.getQuestionnaire(questionnaireId),
        PageHelper.toPageable(page, size)
      ), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(value = "/{questionnaireId}/appraiser",
               produces = MediaType.APPLICATION_JSON_VALUE,
               consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireParticipantResponse> addAppraiser(
    @PathVariable
      String questionnaireId,
    @RequestBody
      QuestionnaireParticipantRequest questionnaireParticipant
  ) {

    Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaire.getStartDate(),
      questionnaire.getDueDate())
    ){
      return ResponseHelper.toDataResponse(
        HttpStatus.FORBIDDEN,
        QuestionnaireParticipantResponse.builder().build()
      );
    }

    return QuestionnaireParticipantResponseMapper.toDataResponseQuestionnaireParticipantResponse(
      questionnaireService.addQuestionnaireAppraiserToQuestionnaire(
        questionnaireId, questionnaireParticipant.getIdParticipant()),
      HttpStatus.OK
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{questionnaireId}/appraiser" +
                         "/{questionnaireParticipantId}")
  public BaseResponse deleteAppraiser(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String questionnaireParticipantId

  ) {
    Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaire.getStartDate(),
      questionnaire.getDueDate())
    ){
      return ResponseHelper.toBaseResponse(
        HttpStatus.FORBIDDEN
      );
    }

    questionnaireService.deleteQuestionnaireAppraiserFromQuestionnaire(
      questionnaireParticipantId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{questionnaireId}/appraisee",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireParticipantDescriptionResponse> getAppraiseeQuestionnaire(
    @PathVariable
      String questionnaireId,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    return QuestionnaireParticipantResponseMapper.toPagingParticipantDescriptionResponse(
      questionnaireService.getQuestionnaireAppraisee(
        questionnaireService.getQuestionnaire(questionnaireId),
        PageHelper.toPageable(page, size)
      ), fileProperties.getUrlPrefix()

    );
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(value = "/{questionnaireId}/appraisee",
               produces = MediaType.APPLICATION_JSON_VALUE,
               consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionnaireParticipantResponse> addAppraisee(
    @PathVariable
      String questionnaireId,
    @RequestBody
      QuestionnaireParticipantRequest questionnaireParticipant
  ) {
    Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaire.getStartDate(),
      questionnaire.getDueDate())
    ){
      return ResponseHelper.toDataResponse(
        HttpStatus.FORBIDDEN,
        QuestionnaireParticipantResponse.builder().build()
      );
    }
    return QuestionnaireParticipantResponseMapper.toDataResponseQuestionnaireParticipantResponse(
      questionnaireService.addQuestionnaireAppraiseeToQuestionnaire(
        questionnaireId, questionnaireParticipant.getIdParticipant()),
      HttpStatus.OK
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{questionnaireId}/appraisee" +
                         "/{questionnaireParticipantId}")
  public BaseResponse deleteAppraisee(
    @PathVariable
      String questionnaireId,
    @PathVariable
      String questionnaireParticipantId
  ) {
    Questionnaire questionnaire = questionnaireService.getQuestionnaire(questionnaireId);
    if(!questionnaireService.validateQuestionnaire(
      questionnaireId,
      questionnaire.getStartDate(),
      questionnaire.getDueDate())
    ){
      return ResponseHelper.toBaseResponse(
        HttpStatus.FORBIDDEN
      );
    }
    questionnaireService.deleteQuestionnaireAppraiseeFromQuestionnaire(
      questionnaireParticipantId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
}
