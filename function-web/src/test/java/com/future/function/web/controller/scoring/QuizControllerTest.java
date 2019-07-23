package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuizRequestMapper;
import com.future.function.web.mapper.response.scoring.QuizResponseMapper;
import com.future.function.web.model.request.scoring.CopyQuizWebRequest;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(QuizController.class)
public class QuizControllerTest extends TestHelper {

  private static final String QUIZ_ID = "quiz-id";
    private static final String QUIZ_TITLE = "quiz-title";
    private static final String QUIZ_DESCRIPTION = "quiz-description";
  private static final long QUIZ_DATE = 150000;
  private static final long QUIZ_TIME_LIMIT = 150000;
  private static final int QUIZ_TRIALS = 3;
  private static final String QUIZ_QUESTION_BANK_ID = "question-bank-id";
  private static final int QUIZ_QUESTION_COUNT = 3;
  private static final String QUIZ_BATCH_CODE = "3";
    private static final String QUIZ_BATCH_ID = "batchId";

  private Pageable pageable;
  private Quiz quiz;
  private Batch batch;
  private QuizWebRequest quizWebRequest;
  private CopyQuizWebRequest copyQuizWebRequest;
  private List<Quiz> quizList;
  private Page<Quiz> quizPage;
  private QuestionBank questionBank;
  private List<String> QUESTION_BANK_IDS;

  private DataResponse<QuizWebResponse> DATA_RESPONSE;
  private DataResponse<QuizWebResponse> CREATED_DATA_RESPONSE;

  private PagingResponse<QuizWebResponse> PAGING_RESPONSE;

  private BaseResponse BASE_RESPONSE;

  private JacksonTester<QuizWebRequest> webRequestJacksonTester;

  private JacksonTester<CopyQuizWebRequest> copyQuizWebRequestJacksonTester;

  @MockBean
  private QuizService quizService;

  @MockBean
  private QuizRequestMapper requestMapper;

  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.ADMIN);

    questionBank = QuestionBank
        .builder()
        .id(QUIZ_QUESTION_BANK_ID)
        .build();

    batch = Batch
        .builder()
        .code(QUIZ_BATCH_CODE)
        .build();

    QUESTION_BANK_IDS = new ArrayList<>();
    QUESTION_BANK_IDS.add(QUIZ_QUESTION_BANK_ID);

    quiz = Quiz
        .builder()
        .id(QUIZ_ID)
        .title(QUIZ_TITLE)
        .description(QUIZ_DESCRIPTION)
        .startDate(QUIZ_DATE)
        .endDate(QUIZ_DATE)
        .timeLimit(QUIZ_TIME_LIMIT)
        .questionCount(QUIZ_QUESTION_COUNT)
        .trials(QUIZ_TRIALS)
        .questionBanks(Collections.singletonList(questionBank))
        .batch(batch)
        .build();

    quizWebRequest = QuizWebRequest
        .builder()
        .title(QUIZ_TITLE)
        .description(QUIZ_DESCRIPTION)
        .startDate(QUIZ_DATE)
        .endDate(QUIZ_DATE)
        .timeLimit(QUIZ_TIME_LIMIT)
        .questionCount(QUIZ_QUESTION_COUNT)
        .trials(QUIZ_TRIALS)
        .questionBanks(QUESTION_BANK_IDS)
        .build();

    copyQuizWebRequest = CopyQuizWebRequest
        .builder()
        .quizId(QUIZ_ID)
            .batchId(QUIZ_BATCH_ID)
        .build();

    quizList = new ArrayList<>();
    quizList.add(quiz);

    pageable = new PageRequest(0, 10);

    quizPage = new PageImpl<>(quizList, pageable, 10);

    DATA_RESPONSE = QuizResponseMapper
        .toQuizWebDataResponse(this.quiz);

    CREATED_DATA_RESPONSE = QuizResponseMapper
        .toQuizWebDataResponse(HttpStatus.CREATED, this.quiz);

    PAGING_RESPONSE = QuizResponseMapper
        .toQuizWebPagingResponse(quizPage);

    BASE_RESPONSE = ResponseHelper.toBaseResponse(HttpStatus.OK);

    when(quizService.findById(QUIZ_ID)).thenReturn(quiz);
    when(quizService.createQuiz(quiz)).thenReturn(quiz);
    when(quizService.updateQuiz(quiz)).thenReturn(quiz);
    when(quizService.findAllByBatchCodeAndPageable(QUIZ_BATCH_CODE, pageable))
        .thenReturn(quizPage);
      when(quizService.copyQuizWithTargetBatchId(QUIZ_BATCH_ID, quiz)).thenReturn(quiz);
      when(requestMapper.toQuiz(quizWebRequest, QUIZ_BATCH_CODE)).thenReturn(quiz);
      when(requestMapper.toQuiz(QUIZ_ID, quizWebRequest, QUIZ_BATCH_CODE)).thenReturn(quiz);
    when(requestMapper.validateCopyQuizWebRequest(copyQuizWebRequest)).thenReturn(copyQuizWebRequest);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(quizService, requestMapper);
  }

  @Test
  public void testFindQuizById() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes/" + QUIZ_ID)
            .cookie(cookies))
        .andExpect(status().isOk())
        .andExpect(content().json(
            dataResponseJacksonTester.write(DATA_RESPONSE)
                .getJson()
        ));

    verify(quizService).findById(QUIZ_ID);
  }

  @Test
  public void testDeleteQuizById() throws Exception {
    mockMvc.perform(
        delete("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes/" + QUIZ_ID)
            .cookie(cookies))
        .andExpect(status().isOk())
        .andExpect(content().json(
            baseResponseJacksonTester.write(BASE_RESPONSE)
                .getJson()
        ));

    verify(quizService).deleteById(QUIZ_ID);
  }

  @Test
  public void testFindAllQuizByPagingParameters() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes")
            .cookie(cookies)
            .param("page", "1")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            pagingResponseJacksonTester.write(PAGING_RESPONSE)
                .getJson()
        ));

    verify(quizService).findAllByBatchCodeAndPageable(QUIZ_BATCH_CODE, pageable);
  }

  @Test
  public void testFindAllQuizWithoutPagingParameters() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes")
            .cookie(cookies))
        .andExpect(status().isOk())
        .andExpect(content().json(
            pagingResponseJacksonTester.write(PAGING_RESPONSE)
                .getJson()
        ));

    verify(quizService).findAllByBatchCodeAndPageable(QUIZ_BATCH_CODE, pageable);
  }

  @Test
  public void testCreateQuizPassQuizWebRequest() throws Exception {
    mockMvc.perform(
        post("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes")
            .cookie(cookies)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(webRequestJacksonTester.write(
                quizWebRequest).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().json(
            dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                .getJson()
        ));

    verify(quizService).createQuiz(quiz);
      verify(requestMapper).toQuiz(quizWebRequest, QUIZ_BATCH_CODE);
  }

  @Test
  public void testCopyQuizWithTargetBatch() throws Exception {
    mockMvc.perform(
        post("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes/copy")
            .cookie(cookies)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(copyQuizWebRequestJacksonTester.write(
                copyQuizWebRequest).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().json(
            dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                .getJson()
        ));
    verify(quizService).findById(QUIZ_ID);
      verify(quizService).copyQuizWithTargetBatchId(QUIZ_BATCH_ID, quiz);
    verify(requestMapper).validateCopyQuizWebRequest(copyQuizWebRequest);
  }

  @Test
  public void testUpdateQuizPassQuizWebRequestAndId() throws Exception {
    mockMvc.perform(
        put("/api/scoring/batches/" + QUIZ_BATCH_CODE + "/quizzes/" + QUIZ_ID)
            .cookie(cookies)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(webRequestJacksonTester.write(
                quizWebRequest).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().json(
            dataResponseJacksonTester.write(DATA_RESPONSE)
                .getJson()
        ));

    verify(quizService).updateQuiz(quiz);
      verify(requestMapper).toQuiz(QUIZ_ID, quizWebRequest, QUIZ_BATCH_CODE);
  }
}