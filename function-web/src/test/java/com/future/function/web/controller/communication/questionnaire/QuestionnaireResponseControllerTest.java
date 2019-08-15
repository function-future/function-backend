package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResultService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSimpleSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSummaryDescriptionResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(QuestionnaireResponseController.class)
public class QuestionnaireResponseControllerTest extends TestHelper {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String BATCH_ID = "batchId";

  private static final String USER_SUMMARY_ID_1 = "userSummaryId1";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION =
    "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID =
    "questionnaireResponseSummaryId1";

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

  private static final String QUESTION_RESPONSE_SUMMARY_ID =
    "questionQuestionnaireSummaryResponseid1";

  private static final Answer SCORE = Answer.builder()
    .minimum(0)
    .maximum(6)
    .average(3)
    .build();

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

  private static final UserQuestionnaireSummary USER_SUMMARY_1 =
    UserQuestionnaireSummary.builder()
      .id(USER_SUMMARY_ID_1)
      .appraisee(MEMBER_1)
      .scoreSummary(SCORE)
      .build();

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final QuestionnaireResponseSummary
    QUESTIONNAIRE_RESPONSE_SUMMARY = QuestionnaireResponseSummary.builder()
    .id(QUESTIONNAIRE_RESPONSE_SUMMARY_ID)
    .questionnaire(QUESTIONNAIRE)
    .appraisee(MEMBER_1)
    .scoreSummary(SCORE)
    .build();

  private static final QuestionQuestionnaire QUESTION_QUESTIONNAIRE =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .questionnaire(QUESTIONNAIRE)
      .description(QUESTION_DESCRIPTION)
      .build();

  private static final QuestionResponseSummary QUESTION_RESPONSE_SUMMARY =
    QuestionResponseSummary.builder()
      .id(QUESTION_RESPONSE_SUMMARY_ID)
      .question(QUESTION_QUESTIONNAIRE)
      .questionnaire(QUESTIONNAIRE)
      .scoreSummary(SCORE)
      .build();

  private final Pageable PAGEABLE = new PageRequest(0, 10);

  private final Page<QuestionnaireResponseSummary> QUESTIONNAIRE_SUMMARY_PAGE =
    new PageImpl<>(Arrays.asList(QUESTIONNAIRE_RESPONSE_SUMMARY), PAGEABLE, 1);

  @MockBean
  private QuestionnaireResponseSummaryService
    questionnaireResponseSummaryService;

  @MockBean
  private QuestionnaireResultService questionnaireResultService;

  @MockBean
  private FileProperties fileProperties;

  @Override
  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.ADMIN);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(questionnaireResponseSummaryService,
                             questionnaireResultService, fileProperties
    );
  }

  @Test
  public void getQuestionnairesSimpleSummary() throws Exception {

    when(questionnaireResultService.getAppraisalsQuestionnaireSummaryById(
      USER_SUMMARY_ID_1)).thenReturn(USER_SUMMARY_1);
    when(
      questionnaireResponseSummaryService.getQuestionnairesSummariesBasedOnAppraisee(
        MEMBER_1, PAGEABLE)).thenReturn(QUESTIONNAIRE_SUMMARY_PAGE);

    PagingResponse<QuestionnaireSimpleSummaryResponse> response =
      QuestionnaireResponseMapper.toPagingQuestionnaireSimpleSummaryResponse(
        QUESTIONNAIRE_SUMMARY_PAGE, HttpStatus.OK);

    mockMvc.perform(get("/api/communication/questionnaire-response").cookie(
      cookies)
                      .param("userSummaryId", USER_SUMMARY_ID_1))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(
      questionnaireResponseSummaryService).getQuestionnairesSummariesBasedOnAppraisee(
      MEMBER_1, PAGEABLE);
    verify(questionnaireResultService).getAppraisalsQuestionnaireSummaryById(
      USER_SUMMARY_ID_1);
  }

  @Test
  public void getQuestionnaireSummaryDetail() throws Exception {

    when(
      questionnaireResponseSummaryService.getQuestionnaireResponseSummaryById(
        QUESTIONNAIRE_RESPONSE_SUMMARY_ID)).thenReturn(
      QUESTIONNAIRE_RESPONSE_SUMMARY);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    DataResponse<QuestionnaireSummaryDescriptionResponse> response =
      QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionnaireDataSummaryDescription(
        QUESTIONNAIRE_RESPONSE_SUMMARY, URL_PREFIX);

    mockMvc.perform(get("/api/communication/questionnaire-response/" +
                        QUESTIONNAIRE_RESPONSE_SUMMARY_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(
      questionnaireResponseSummaryService).getQuestionnaireResponseSummaryById(
      QUESTIONNAIRE_RESPONSE_SUMMARY_ID);
  }

  @Test
  public void getQuestionSummaryResponse() throws Exception {

    when(questionnaireResultService.getAppraisalsQuestionnaireSummaryById(
      USER_SUMMARY_ID_1)).thenReturn(USER_SUMMARY_1);

    when(
      questionnaireResponseSummaryService.getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(
        QUESTIONNAIRE_RESPONSE_SUMMARY_ID, MEMBER_1)).thenReturn(
      Arrays.asList(QUESTION_RESPONSE_SUMMARY));

    DataResponse<List<QuestionQuestionnaireSummaryResponse>> response =
      QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionQuestionnaireSummaryResponseList(
        Arrays.asList(QUESTION_RESPONSE_SUMMARY));

    mockMvc.perform(get("/api/communication/questionnaire-response/" +
                        QUESTIONNAIRE_RESPONSE_SUMMARY_ID + "/questions/" +
                        USER_SUMMARY_ID_1).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));


    verify(
      questionnaireResponseSummaryService).getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(
      QUESTIONNAIRE_RESPONSE_SUMMARY_ID, MEMBER_1);

    verify(questionnaireResultService).getAppraisalsQuestionnaireSummaryById(
      USER_SUMMARY_ID_1);
  }

}
