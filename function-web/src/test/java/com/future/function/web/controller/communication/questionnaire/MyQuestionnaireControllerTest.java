package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.communication.questionnaire.MyQuestionnaireRequestMapper;
import com.future.function.web.mapper.response.communication.questionnaire.MyQuestionnaireResponseMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.model.request.communication.questionnaire.QuestionResponseRequest;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireResponseRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraisalDataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraiseeResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(MyQuestionnaireController.class)
public class MyQuestionnaireControllerTest extends TestHelper {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String USER_ID = "userId";

  private static final User USER = User.builder()
    .id(USER_ID)
    .build();

  private static final Pageable PAGEABLE = PageHelper.toPageable(1, 10);

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION =
    "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTIONNAIRE_PARTICIPANT_ID =
    "questionniareParticipantId";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_2 =
    "questionniareParticipantId2";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

  private static final String COMMENT = "comment";

  private static final String QUESTION_RESPONSE_ID = "questionResponseId";

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final PageImpl<Questionnaire> QUESTIONNAIRE_PAGE =
    new PageImpl<>(Collections.singletonList(QUESTIONNAIRE), PAGEABLE, 1);

  private static final User MEMBER_1 = User.builder()
    .id(MEMBER_ID_1)
    .name(MEMBER_NAME_1)
    .pictureV2(FileV2.builder()
                 .thumbnailUrl(THUMBNAIL_URL)
                 .build())
    .batch(Batch.builder()
             .id(BATCH_ID)
             .build())
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final User MEMBER_2 = User.builder()
    .id(MEMBER_ID_2)
    .name(MEMBER_NAME_2)
    .pictureV2(FileV2.builder()
                 .thumbnailUrl(THUMBNAIL_URL)
                 .build())
    .role(Role.MENTOR)
    .build();

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID)
      .questionnaire(QUESTIONNAIRE)
      .member(MEMBER_1)
      .participantType(ParticipantType.APPRAISER)
      .build();

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT_2 =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_2)
      .questionnaire(QUESTIONNAIRE)
      .member(MEMBER_2)
      .participantType(ParticipantType.APPRAISEE)
      .build();

  private static final QuestionQuestionnaire QUESTION_QUESTIONNAIRE =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .questionnaire(QUESTIONNAIRE)
      .description(QUESTION_DESCRIPTION)
      .build();

  private static final QuestionResponseRequest QUESTION_RESPONSE_REQUEST =
    QuestionResponseRequest.builder()
      .idQuestion(QUESTION_ID)
      .score(Float.valueOf(3))
      .comment(COMMENT)
      .build();

  private static final QuestionResponse QUESTION_RESPONSE =
    QuestionResponse.builder()
      .id(QUESTION_RESPONSE_ID)
      .build();

  private final QuestionnaireResponseRequest
    QUESTIONNAIRE_RESPONSE_WEB_REQUEST = QuestionnaireResponseRequest.builder()
    .responses(Arrays.asList(QUESTION_RESPONSE_REQUEST))
    .build();


  @MockBean
  private MyQuestionnaireService myQuestionnaireService;

  @MockBean
  private QuestionnaireService questionnaireService;

  @MockBean
  private UserService userService;

  @MockBean
  private MyQuestionnaireRequestMapper myQuestionnaireRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  private JacksonTester<QuestionnaireResponseRequest>
    questionnaireResponseRequestJaksonTester;

  @Override
  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.STUDENT);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(myQuestionnaireService, questionnaireService,
                             userService, myQuestionnaireRequestMapper,
                             fileProperties
    );
  }

  @Test
  public void getMyQuestionnaires() throws Exception {

    when(userService.getUser(any(String.class))).thenReturn(USER);
    when(myQuestionnaireService.getQuestionnairesByMemberLoginAsAppraiser(USER,
                                                                          PAGEABLE
    )).thenReturn(QUESTIONNAIRE_PAGE);
    PagingResponse<QuestionnaireDetailResponse> response =
      QuestionnaireResponseMapper.toPagingQuestionnaireDetailResponse(
        QUESTIONNAIRE_PAGE);

    mockMvc.perform(get("/api/communication/my-questionnaires").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(userService).getUser(any(String.class));
    verify(myQuestionnaireService).getQuestionnairesByMemberLoginAsAppraiser(
      USER, PAGEABLE);
  }

  @Test
  public void getListAprraisees() throws Exception {

    when(userService.getUser(any(String.class))).thenReturn(USER);
    when(questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID_1)).thenReturn(
      QUESTIONNAIRE);
    when(
      myQuestionnaireService.getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(
        QUESTIONNAIRE, USER)).thenReturn(
      Arrays.asList(QUESTIONNAIRE_PARTICIPANT, QUESTIONNAIRE_PARTICIPANT_2));
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    DataResponse<List<AppraiseeResponse>> response =
      MyQuestionnaireResponseMapper.toDataResponseAppraiseeResponseList(
        Arrays.asList(QUESTIONNAIRE_PARTICIPANT, QUESTIONNAIRE_PARTICIPANT_2),
        URL_PREFIX
      );

    mockMvc.perform(get(
      "/api/communication/my-questionnaires/" + QUESTIONNAIRE_ID_1 +
      "/appraisees").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));

    verify(userService).getUser(any(String.class));
    verify(questionnaireService).getQuestionnaire(QUESTIONNAIRE_ID_1);
    verify(
      myQuestionnaireService).getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(
      QUESTIONNAIRE, USER);
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void getQuestionnaireData() throws Exception {

    when(userService.getUser(MEMBER_ID_1)).thenReturn(MEMBER_1);
    when(questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID_1)).thenReturn(
      QUESTIONNAIRE);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    DataResponse<AppraisalDataResponse> response =
      MyQuestionnaireResponseMapper.toDataResponseQuestionnaireSummaryDescriptionResponse(
        QUESTIONNAIRE, MEMBER_1, URL_PREFIX);

    mockMvc.perform(get(
      "/api/communication/my-questionnaires/" + QUESTIONNAIRE_ID_1 +
      "/appraisees/" + MEMBER_ID_1).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));


    verify(fileProperties).getUrlPrefix();
    verify(userService).getUser(MEMBER_ID_1);
    verify(questionnaireService).getQuestionnaire(QUESTIONNAIRE_ID_1);

  }

  @Test
  public void getQuestion() throws Exception {

    when(questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID_1)).thenReturn(
      QUESTIONNAIRE);
    when(myQuestionnaireService.getQuestionsFromQuestionnaire(
      QUESTIONNAIRE)).thenReturn(Arrays.asList(QUESTION_QUESTIONNAIRE));

    DataResponse<List<QuestionQuestionnaireResponse>> response =
      MyQuestionnaireResponseMapper.toDataResponseQuestionQuestionnaireResponseList(
        Arrays.asList(QUESTION_QUESTIONNAIRE));

    mockMvc.perform(get(
      "/api/communication/my-questionnaires/" + QUESTIONNAIRE_ID_1 +
      "/appraisees/" + MEMBER_ID_1 + "/questions").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));


    verify(questionnaireService).getQuestionnaire(QUESTIONNAIRE_ID_1);
    verify(myQuestionnaireService).getQuestionsFromQuestionnaire(QUESTIONNAIRE);

  }

  @Test
  public void addQuestionnaireResponse() throws Exception {

    when(userService.getUser(any(String.class))).thenReturn(MEMBER_1);
    when(questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID_1)).thenReturn(
      QUESTIONNAIRE);
    when(myQuestionnaireRequestMapper.toListQuestionResponse(
      Arrays.asList(QUESTION_RESPONSE_REQUEST), MEMBER_1, MEMBER_1)).thenReturn(
      Arrays.asList(QUESTION_RESPONSE));

    when(
      myQuestionnaireService.createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
        QUESTIONNAIRE, Arrays.asList(QUESTION_RESPONSE), MEMBER_1,
        MEMBER_1
      )).thenReturn(null);

    mockMvc.perform(post(
      "/api/communication/my-questionnaires/" + QUESTIONNAIRE_ID_1 +
      "/appraisees/" + MEMBER_ID_1 + "/questions").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(questionnaireResponseRequestJaksonTester.write(
                        QUESTIONNAIRE_RESPONSE_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated());

    verify(questionnaireService).getQuestionnaire(QUESTIONNAIRE_ID_1);
    verify(userService, times(4)).getUser(any(String.class));
    verify(
      myQuestionnaireService).createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
      QUESTIONNAIRE, Arrays.asList(QUESTION_RESPONSE), MEMBER_1, MEMBER_1);
    verify(myQuestionnaireRequestMapper).toListQuestionResponse(
      Arrays.asList(QUESTION_RESPONSE_REQUEST), MEMBER_1, MEMBER_1);
  }

}
