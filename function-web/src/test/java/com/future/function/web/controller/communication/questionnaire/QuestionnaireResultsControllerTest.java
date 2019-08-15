package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireResultService;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResultsResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.UserSummaryResponse;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(QuestionnaireResultsController.class)
public class QuestionnaireResultsControllerTest extends TestHelper {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String BATCH_ID = "batchId";

  private static final String BATCH_CODE = "batchCode";

  private static final String BATCH_NAME = "batchName";

  private static final String USER_SUMMARY_ID_1 = "userSummaryId1";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String UNIVERSITY = "itb";

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

  private final Pageable PAGEABLE = new PageRequest(0, 10);

  private final Batch BATCH = Batch.builder()
    .id(BATCH_ID)
    .code(BATCH_CODE)
    .name(BATCH_NAME)
    .build();

  private final Page<UserQuestionnaireSummary> USER_QUESTIONNAIRE_SUMMARY_PAGE =
    new PageImpl<>(Arrays.asList(USER_SUMMARY_1), PAGEABLE, 1);

  @MockBean
  private QuestionnaireResultService questionnaireResultService;

  @MockBean
  private BatchService batchService;

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

    verifyNoMoreInteractions(questionnaireResultService, batchService);
  }

  @Test
  public void getUserSummary() throws Exception {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(
      questionnaireResultService.getAppraisalsQuestionnaireSummaryByBatch(BATCH,
                                                                          PAGEABLE
      )).thenReturn(USER_QUESTIONNAIRE_SUMMARY_PAGE);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<UserSummaryResponse> response =
      QuestionnaireResultsResponseMapper.toPagingUserSummaryResponse(
        USER_QUESTIONNAIRE_SUMMARY_PAGE, URL_PREFIX);
    mockMvc.perform(get("/api/communication/questionnaire-results").cookie(
      cookies)
                      .param("batchCode", BATCH_CODE))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(questionnaireResultService).getAppraisalsQuestionnaireSummaryByBatch(
      BATCH, PAGEABLE);
  }

  @Test
  public void getUserSummaryById() throws Exception {

    when(questionnaireResultService.getAppraisalsQuestionnaireSummaryById(
      USER_SUMMARY_ID_1)).thenReturn(USER_SUMMARY_1);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    DataResponse<UserSummaryResponse> response =
      QuestionnaireResultsResponseMapper.toDataResponseUserSummaryResponse(
        USER_SUMMARY_1, URL_PREFIX);

    mockMvc.perform(get(
      "/api/communication/questionnaire-results/" + BATCH_CODE +
      "/user-summary-response/" + USER_SUMMARY_ID_1).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(questionnaireResultService).getAppraisalsQuestionnaireSummaryById(
      USER_SUMMARY_ID_1);
  }

}
