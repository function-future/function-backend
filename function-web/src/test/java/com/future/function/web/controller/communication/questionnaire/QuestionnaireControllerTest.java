package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(QuestionnaireController.class)
public class QuestionnaireControllerTest extends TestHelper {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String BATCH_ID = "batchId";

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final String KEYWORD = "keyword";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

  private static final User MEMBER_1 = User.builder()
    .id(MEMBER_ID_1)
    .name(MEMBER_NAME_1)
    .pictureV2(FileV2.builder().thumbnailUrl(THUMBNAIL_URL).build())
    .batch(Batch.builder().id(BATCH_ID).build())
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final Page<Questionnaire> QUESTIONNAIRE_PAGE =
    new PageImpl<>(Arrays.asList(QUESTIONNAIRE), PAGEABLE, 1);

  private QuestionnaireRequest QUESTIONNAIRE_REQUEST =
    QuestionnaireRequest.builder()
      .title(QUESTIONNAIRE_TITLE)
      .desc(QUESTIONNAIRE_DESCRIPTION)
      .startDate(START_DATE)
      .dueDate(DUE_DATE)
      .build();

  private static final BaseResponse BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);

  private static final QuestionQuestionnaire QUESTION_QUESTIONNAIRE =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .questionnaire(QUESTIONNAIRE)
      .description(QUESTION_DESCRIPTION)
      .build();

  private static final QuestionQuestionnaireRequest QUESTION_QUESTIONNAIRE_REQUEST =
    QuestionQuestionnaireRequest.builder()
      .description(QUESTION_DESCRIPTION)
      .build();

  private static final String QUESTIONNAIRE_PARTICIPANT_ID = "questionniareParticipantId";

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID)
      .questionnaire(QUESTIONNAIRE)
      .member(MEMBER_1)
      .participantType(ParticipantType.APPRAISER)
      .build();

  private static final Page<QuestionnaireParticipant> QUESTIONNAIRE_PARTICIPANT_PAGE =
    new PageImpl<>(Arrays.asList(QUESTIONNAIRE_PARTICIPANT), PAGEABLE, 1);

  private static final QuestionnaireParticipantRequest QUESTIONNAIRE_PARTICIPANT_REQUEST =
    QuestionnaireParticipantRequest.builder()
      .idParticipant(MEMBER_ID_1)
      .build();

  @MockBean
  private QuestionnaireService questionnaireService;

  @MockBean
  private UserService userService;

  @MockBean
  private QuestionnaireRequestMapper questionnaireRequestMapper;

  @MockBean
  private QuestionQuestionnaireRequestMapper questionQuestionnaireRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  private JacksonTester<QuestionnaireRequest> questionnaireRequestJacksonTester;

  private JacksonTester<QuestionQuestionnaireRequest> questionQuestionnaireRequestJacksonTester;

  private JacksonTester<QuestionnaireParticipantRequest> questionnaireParticipantRequestJacksonTester;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.ADMIN);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(
      questionnaireService,
      userService,
      questionnaireRequestMapper,
      questionQuestionnaireRequestMapper,
      fileProperties
    );
  }

  @Test
  public void getQuestionnaires() throws Exception {

    when(
      questionnaireService
        .getQuestionnairesWithKeyword(
          KEYWORD, PAGEABLE))
      .thenReturn(QUESTIONNAIRE_PAGE);

    when(
      questionnaireService
        .getAllQuestionnaires(PAGEABLE))
      .thenReturn(QUESTIONNAIRE_PAGE);

    PagingResponse<QuestionnaireDetailResponse> response =
      QuestionnaireResponseMapper
        .toPagingQuestionnaireDetailResponse(QUESTIONNAIRE_PAGE);

    mockMvc.perform(
      get("/api/communication/questionnaires")
        .cookie(cookies).param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    mockMvc.perform(
      get("/api/communication/questionnaires")
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService)
      .getQuestionnairesWithKeyword(
        KEYWORD, PAGEABLE);

    verify(questionnaireService)
      .getAllQuestionnaires(PAGEABLE);
  }

  @Test
  public void createQuestionnaires() throws Exception {

    when(
      questionnaireService
        .createQuestionnaire(
          any(Questionnaire.class),
          any(User.class)
        ))
      .thenReturn(QUESTIONNAIRE);

    when(userService.getUser(any(String.class)))
      .thenReturn(MEMBER_1);

    DataResponse<QuestionnaireDetailResponse> response =
      QuestionnaireResponseMapper
        .toDataResponseQuestionnaireDetailResponse(
          QUESTIONNAIRE, HttpStatus.CREATED);

    mockMvc.perform(post("/api/communication/questionnaires").cookie(cookies)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(questionnaireRequestJacksonTester.write(QUESTIONNAIRE_REQUEST).getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService)
      .createQuestionnaire(
        any(Questionnaire.class),
        any(User.class));

    verify(userService).getUser(any(String.class));
  }

  @Test
  public void getQuestionnaire() throws Exception {
    when(
      questionnaireService
        .getQuestionnaire(QUESTIONNAIRE_ID_1))
      .thenReturn(QUESTIONNAIRE);

    DataResponse<QuestionnaireDetailResponse> response =
      QuestionnaireResponseMapper
        .toDataResponseQuestionnaireDetailResponse(QUESTIONNAIRE, HttpStatus.OK);

    mockMvc.perform(
      get("/api/communication/questionnaires/"
        +QUESTIONNAIRE_ID_1
      )
        .cookie(cookies).param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService)
      .getQuestionnaire(QUESTIONNAIRE_ID_1);
  }

  @Test
  public void updateQuestionnaire() throws Exception {
    when(
      questionnaireRequestMapper
        .toQuestionnaire(QUESTIONNAIRE_REQUEST, QUESTIONNAIRE_ID_1))
      .thenReturn(QUESTIONNAIRE);

    when(
      questionnaireService.updateQuestionnaire(QUESTIONNAIRE))
      .thenReturn(QUESTIONNAIRE);

    DataResponse<QuestionnaireDetailResponse> response =
      QuestionnaireResponseMapper
        .toDataResponseQuestionnaireDetailResponse(
          QUESTIONNAIRE, HttpStatus.OK);

    mockMvc.perform(
      put("/api/communication/questionnaires/"
          +QUESTIONNAIRE_ID_1
      ).cookie(cookies)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(questionnaireRequestJacksonTester.write(QUESTIONNAIRE_REQUEST).getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireRequestMapper)
      .toQuestionnaire(QUESTIONNAIRE_REQUEST, QUESTIONNAIRE_ID_1);

    verify(questionnaireService)
      .updateQuestionnaire(QUESTIONNAIRE);
  }

  @Test
  public void deleteQuestionnaire() throws Exception {

    doNothing().when(questionnaireService).deleteQuestionnaire(QUESTIONNAIRE_ID_1);
    mockMvc.perform(
      delete("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(BASE_RESPONSE).getJson()))
      .andReturn()
      .getResponse();
    verify(questionnaireService).deleteQuestionnaire(QUESTIONNAIRE_ID_1);
  }

  @Test
  public void getQuestionsQuestionnaire() throws Exception {
    when(questionnaireService.getQuestionsByIdQuestionnaire(QUESTIONNAIRE_ID_1))
      .thenReturn(Arrays.asList(QUESTION_QUESTIONNAIRE));

    DataResponse<List<QuestionQuestionnaireResponse>> response =
      QuestionQuestionnaireResponseMapper
        .toDataResponseListQuestionQuestionnaireResponse(Arrays.asList(QUESTION_QUESTIONNAIRE), HttpStatus.OK);

    mockMvc.perform(
      get("/api/communication/questionnaires/"
        +QUESTIONNAIRE_ID_1
        +"/questions"
      )
        .cookie(cookies).param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService).getQuestionsByIdQuestionnaire(QUESTIONNAIRE_ID_1);
  }

  @Test
  public void createQuestionQuestionnaire() throws Exception {

    when(questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID_1))
      .thenReturn(QUESTIONNAIRE);

    when(
        questionQuestionnaireRequestMapper.toQuestionQuestionnaire(
        any(QuestionQuestionnaireRequest.class),
        any(),
        any(Questionnaire.class)))
      .thenReturn(QUESTION_QUESTIONNAIRE);

    when(questionnaireService.createQuestionQuestionnaire(QUESTION_QUESTIONNAIRE))
      .thenReturn(QUESTION_QUESTIONNAIRE);

    DataResponse<QuestionQuestionnaireResponse> response =
      QuestionQuestionnaireResponseMapper
        .toDataResponseQuestionQuestionnaireResponse(
          QUESTION_QUESTIONNAIRE, HttpStatus.CREATED);

    mockMvc.perform(
      post("/api/communication/questionnaires/"
            + QUESTIONNAIRE_ID_1
            + "/questions"
      ).cookie(cookies)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(questionQuestionnaireRequestJacksonTester.write(QUESTION_QUESTIONNAIRE_REQUEST).getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService).getQuestionnaire(QUESTIONNAIRE_ID_1);
    verify(questionQuestionnaireRequestMapper)
      .toQuestionQuestionnaire(
        any(QuestionQuestionnaireRequest.class),
        any(),
        any(Questionnaire.class));

    verify(questionnaireService).createQuestionQuestionnaire(QUESTION_QUESTIONNAIRE);
  }

  @Test
  public void updateQuestionQuestionnaire() throws Exception {

    when(
      questionnaireService
        .getQuestionnaire(QUESTIONNAIRE_ID_1))
      .thenReturn(QUESTIONNAIRE);
    when(
      questionQuestionnaireRequestMapper.toQuestionQuestionnaire(
        QUESTION_QUESTIONNAIRE_REQUEST,
        QUESTION_ID,
        QUESTIONNAIRE))
      .thenReturn(QUESTION_QUESTIONNAIRE);

    when(questionnaireService.updateQuestionQuestionnaire(QUESTION_QUESTIONNAIRE))
      .thenReturn(QUESTION_QUESTIONNAIRE);

    DataResponse<QuestionQuestionnaireResponse> response =
      QuestionQuestionnaireResponseMapper.toDataResponseQuestionQuestionnaireResponse(
        QUESTION_QUESTIONNAIRE, HttpStatus.OK
      );

    mockMvc.perform(
      put("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1
        + "/questions/"
        + QUESTION_ID
      ).cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(questionQuestionnaireRequestJacksonTester.write(QUESTION_QUESTIONNAIRE_REQUEST).getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService)
      .getQuestionnaire(QUESTIONNAIRE_ID_1);
    verify(questionQuestionnaireRequestMapper).toQuestionQuestionnaire(
      QUESTION_QUESTIONNAIRE_REQUEST,
      QUESTION_ID,
      QUESTIONNAIRE);
    verify(questionnaireService).updateQuestionQuestionnaire(QUESTION_QUESTIONNAIRE);
  }

  @Test
  public void deleteQuestionQuestionnaire() throws Exception {
    doNothing().when(questionnaireService).deleteQuestionQuestionnaire(QUESTION_ID);
    mockMvc.perform(
      delete("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1
        + "/questions/"
        + QUESTION_ID))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(BASE_RESPONSE).getJson()))
      .andReturn()
      .getResponse();
    verify(questionnaireService).deleteQuestionQuestionnaire(QUESTION_ID);
  }

  @Test
  public void getAppraiserQuestionnaire() throws Exception {

    when(
      questionnaireService
        .getQuestionnaire(QUESTIONNAIRE_ID_1))
      .thenReturn(QUESTIONNAIRE);

    when(questionnaireService.getQuestionnaireAppraiser(QUESTIONNAIRE, PAGEABLE))
      .thenReturn(QUESTIONNAIRE_PARTICIPANT_PAGE);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    PagingResponse<QuestionnaireParticipantDescriptionResponse> response =
      QuestionnaireParticipantResponseMapper
        .toPagingParticipantDescriptionResponse(
          QUESTIONNAIRE_PARTICIPANT_PAGE,
          URL_PREFIX
        );

    mockMvc.perform(
      get("/api/communication/questionnaires/"
        +QUESTIONNAIRE_ID_1
        +"/appraiser"
      )
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(questionnaireService)
      .getQuestionnaire(QUESTIONNAIRE_ID_1);
    verify(questionnaireService).getQuestionnaireAppraiser(QUESTIONNAIRE, PAGEABLE);
  }

  @Test
  public void addAppraiser() throws Exception {

    when(questionnaireService.addQuestionnaireAppraiserToQuestionnaire(
      QUESTIONNAIRE_ID_1, MEMBER_ID_1
    )).thenReturn(QUESTIONNAIRE_PARTICIPANT);

    DataResponse<QuestionnaireParticipantResponse> response =
      QuestionnaireParticipantResponseMapper
        .toDataResponseQuestionnaireParticipantResponse(QUESTIONNAIRE_PARTICIPANT, HttpStatus.OK);

    mockMvc.perform(
      post("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1
        + "/appraiser"
      ).cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(questionnaireParticipantRequestJacksonTester.write(QUESTIONNAIRE_PARTICIPANT_REQUEST).getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService).addQuestionnaireAppraiserToQuestionnaire(
      QUESTIONNAIRE_ID_1, MEMBER_ID_1);
  }

  @Test
  public void deleteAppraiser() throws Exception {
    doNothing().when(questionnaireService).deleteQuestionnaireAppraiserFromQuestionnaire(QUESTIONNAIRE_PARTICIPANT_ID);
    mockMvc.perform(
      delete("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1
        + "/appraiser/"
        + QUESTIONNAIRE_PARTICIPANT_ID))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(BASE_RESPONSE).getJson()))
      .andReturn()
      .getResponse();
    verify(questionnaireService).deleteQuestionnaireAppraiserFromQuestionnaire(QUESTIONNAIRE_PARTICIPANT_ID);
  }

  @Test
  public void getAppraiseeQuestionnaire() throws Exception {

    when(
      questionnaireService
        .getQuestionnaire(QUESTIONNAIRE_ID_1))
      .thenReturn(QUESTIONNAIRE);

    when(questionnaireService.getQuestionnaireAppraisee(QUESTIONNAIRE, PAGEABLE))
      .thenReturn(QUESTIONNAIRE_PARTICIPANT_PAGE);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<QuestionnaireParticipantDescriptionResponse> response =
      QuestionnaireParticipantResponseMapper
        .toPagingParticipantDescriptionResponse(
          QUESTIONNAIRE_PARTICIPANT_PAGE,
          URL_PREFIX
        );

    mockMvc.perform(
      get("/api/communication/questionnaires/"
        +QUESTIONNAIRE_ID_1
        +"/appraisee"
      )
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(questionnaireService)
      .getQuestionnaire(QUESTIONNAIRE_ID_1);

    verify(questionnaireService).getQuestionnaireAppraisee(QUESTIONNAIRE, PAGEABLE);
  }

  @Test
  public void addAppraisee() throws Exception {

    when(questionnaireService.addQuestionnaireAppraiseeToQuestionnaire(
      QUESTIONNAIRE_ID_1, MEMBER_ID_1
    )).thenReturn(QUESTIONNAIRE_PARTICIPANT);

    DataResponse<QuestionnaireParticipantResponse> response =
      QuestionnaireParticipantResponseMapper
        .toDataResponseQuestionnaireParticipantResponse(QUESTIONNAIRE_PARTICIPANT, HttpStatus.OK);

    mockMvc.perform(
      post("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1
        + "/appraisee"
      ).cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(questionnaireParticipantRequestJacksonTester.write(QUESTIONNAIRE_PARTICIPANT_REQUEST).getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireService).addQuestionnaireAppraiseeToQuestionnaire(
      QUESTIONNAIRE_ID_1, MEMBER_ID_1);
  }

  @Test
  public void deleteAppraisee() throws Exception {
    doNothing().when(questionnaireService).deleteQuestionnaireAppraiseeFromQuestionnaire(QUESTIONNAIRE_PARTICIPANT_ID);
    mockMvc.perform(
      delete("/api/communication/questionnaires/"
        + QUESTIONNAIRE_ID_1
        + "/appraisee/"
        + QUESTIONNAIRE_PARTICIPANT_ID))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(BASE_RESPONSE).getJson()))
      .andReturn()
      .getResponse();
    verify(questionnaireService).deleteQuestionnaireAppraiseeFromQuestionnaire(QUESTIONNAIRE_PARTICIPANT_ID);
  }
}
