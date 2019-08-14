package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResponseSummaryService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionAnswerResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
@WebMvcTest(QuestionResponseController.class)
public class QuestionResponseControllerTest extends TestHelper {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

  private static final String QUESTION_RESPONSE_SUMMARY_ID = "questionQuestionnaireSummaryResponseid1";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTION_RESPONSE_ID = "questionResponseId";

  private static final Answer SCORE = Answer.builder()
    .minimum(0)
    .maximum(6)
    .average(3)
    .build();

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


  private static final QuestionQuestionnaire QUESTION_QUESTIONNAIRE =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .questionnaire(QUESTIONNAIRE)
      .description(QUESTION_DESCRIPTION)
      .build();

  private static final QuestionResponse QUESTION_RESPONSE =
    QuestionResponse.builder()
      .id(QUESTION_RESPONSE_ID)
      .score(Float.valueOf(3))
      .appraiser(MEMBER_1)
      .question(QUESTION_QUESTIONNAIRE)
      .build();


  private static final QuestionResponseSummary QUESTION_RESPONSE_SUMMARY =
    QuestionResponseSummary.builder()
      .id(QUESTION_RESPONSE_SUMMARY_ID)
      .question(QUESTION_QUESTIONNAIRE)
      .questionnaire(QUESTIONNAIRE)
      .scoreSummary(SCORE)
      .build();

  @MockBean
  private QuestionnaireResponseSummaryService questionnaireResponseSummaryService;

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
    verifyNoMoreInteractions(questionnaireResponseSummaryService, fileProperties );
  }

  @Test
  public void getQuestionQuestionnaireSummaryResponse() throws Exception {
    when(questionnaireResponseSummaryService
      .getQuestionResponseSummaryById(QUESTION_RESPONSE_SUMMARY_ID))
        .thenReturn(QUESTION_RESPONSE_SUMMARY);

    DataResponse<QuestionQuestionnaireSummaryResponse> response =
      QuestionnaireResponseSummaryResponseMapper
        .toDataResponseQuestionQuestionnaireSummaryResponse(QUESTION_RESPONSE_SUMMARY);

    mockMvc.perform(
      get("/api/communication/question-response/"
          +QUESTION_RESPONSE_SUMMARY_ID
      )
      .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(questionnaireResponseSummaryService)
      .getQuestionResponseSummaryById(QUESTION_RESPONSE_SUMMARY_ID);
  }

  @Test
  public void getQuestionnaireAnswerDetailSummary() throws Exception {

    when(questionnaireResponseSummaryService
      .getQuestionResponseByQuestionResponseSummaryId(QUESTION_RESPONSE_SUMMARY_ID))
        .thenReturn(Arrays.asList(QUESTION_RESPONSE));
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    DataResponse<List<QuestionAnswerResponse>> response =
      QuestionnaireResponseSummaryResponseMapper
        .toDataResponseQuestionAnswerDetailResponse(
          Arrays.asList(QUESTION_RESPONSE),
          URL_PREFIX
        );

    mockMvc.perform(
      get("/api/communication/question-response/"
        +QUESTION_RESPONSE_SUMMARY_ID
        +"/responses"
      )
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(questionnaireResponseSummaryService)
      .getQuestionResponseByQuestionResponseSummaryId(QUESTION_RESPONSE_SUMMARY_ID);
  }
}
