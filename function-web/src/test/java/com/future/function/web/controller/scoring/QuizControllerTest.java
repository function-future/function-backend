package com.future.function.web.controller.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuizRequestMapper;
import com.future.function.web.mapper.response.scoring.QuizResponseMapper;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import static org.junit.Assert.*;
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
@WebMvcTest(QuizController.class)
public class QuizControllerTest {

  private static final String QUIZ_TITLE = "assignment-title";
  private static final String QUIZ_DESCRIPTION = "assignment-description";
  private static final long QUIZ_DEADLINE = 150000;
  private static final long QUIZ_TIME_LIMIT = 150000;
  private static final int QUIZ_TRIES = 3;
  private static final int QUIZ_QUESTION_COUNT = 3;
  private static final String QUIZ_BATCH = "[2, 3]";
  private static final String QUIZ_CREATE_REQUEST_JSON =
          "{\n" + "\"title\": \"" + QUIZ_TITLE + "\",\n" + "    \"description\": \"" +
                  QUIZ_DESCRIPTION + "\",\n" + "    \"questionCount\": \"" + QUIZ_QUESTION_COUNT + "\",\n" +
                  "    \"deadline\": " + QUIZ_DEADLINE + ",\n" + "    \"batch\": " + QUIZ_BATCH +
                  ",\n" + "    \"timeLimit\": " + QUIZ_TIME_LIMIT + ",\n" + "    \"tries\": " + QUIZ_TRIES +
                  "}";
  private static String QUIZ_ID = UUID.randomUUID().toString();
  private static final String QUIZ_UPDATE_REQUEST_JSON =
          "{\n" + "\"id\": \"" + QUIZ_ID + "\",\n" + "  \"title\": \"" + QUIZ_TITLE + "\",\n" +
                  "    \"description\": \"" + QUIZ_DESCRIPTION + "\",\n" + "    " +
                  "\"questionCount\": \"" + QUIZ_QUESTION_COUNT + "\",\n" + "    \"deadline\": "
                  + QUIZ_DEADLINE + ",\n" + "    \"batch\": " + QUIZ_BATCH + ",\n" + "    " +
                  "\"timeLimit\": " + QUIZ_TIME_LIMIT + ",\n" + "    \"tries\": " + QUIZ_TRIES +
                  "}";
  private Pageable pageable;
  private Quiz quiz;
  private List<Quiz> quizList;
  private Page<Quiz> quizPage;
  private QuizWebRequest quizWebRequest;

  private DataResponse<QuizWebResponse> DATA_RESPONSE;
  private DataResponse<QuizWebResponse> CREATED_DATA_RESPONSE;

  private PagingResponse<QuizWebResponse> PAGING_RESPONSE;

  private BaseResponse BASE_RESPONSE;

  private JacksonTester<DataResponse<QuizWebResponse>> dataResponseJacksonTester;

  private JacksonTester<PagingResponse<QuizWebResponse>> pagingResponseJacksonTester;

  private JacksonTester<BaseResponse> baseResponseJacksonTester;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private QuizService quizService;

  @MockBean
  private QuizRequestMapper requestMapper;

  @Before
  public void setUp() throws Exception {
    JacksonTester.initFields(this, ObjectMapper::new);

    quiz = Quiz
            .builder()
            .id(QUIZ_ID)
            .title(QUIZ_TITLE)
            .description(QUIZ_DESCRIPTION)
            .deadline(QUIZ_DEADLINE)
            .timeLimit(QUIZ_TIME_LIMIT)
            .questionCount(QUIZ_QUESTION_COUNT)
            .tries(QUIZ_TRIES)
            .build();

    quizWebRequest = new QuizWebRequest();
    BeanUtils.copyProperties(quiz, quizWebRequest);

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
    when(quizService.findAllByPageableAndFilterAndSearch(pageable, "", ""))
            .thenReturn(quizPage);

    when(requestMapper.toQuiz(quizWebRequest)).thenReturn(quiz);
    when(requestMapper.toQuiz(QUIZ_ID, quizWebRequest)).thenReturn(quiz);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(quizService, requestMapper);
  }

  @Test
  public void testFindQuizById() throws Exception {
    mockMvc.perform(
            get("/api/scoring/quiz/" + QUIZ_ID))
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
            delete("/api/scoring/quiz/" + QUIZ_ID))
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
            get("/api/scoring/quiz")
            .param("page", "1")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    pagingResponseJacksonTester.write(PAGING_RESPONSE)
                    .getJson()
            ));

    verify(quizService).findAllByPageableAndFilterAndSearch(pageable, "", "");
  }

  @Test
  public void testFindAllQuizWithoutPagingParameters() throws Exception {
    mockMvc.perform(
            get("/api/scoring/quiz"))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    pagingResponseJacksonTester.write(PAGING_RESPONSE)
                    .getJson()
            ));

    verify(quizService).findAllByPageableAndFilterAndSearch(pageable, "", "");
  }

  @Test
  public void testCreateQuizPassQuizWebRequest() throws Exception {
    mockMvc.perform(
            post("/api/scoring/quiz")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(QUIZ_CREATE_REQUEST_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().json(
                    dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                    .getJson()
            ));

    verify(quizService).createQuiz(quiz);
    verify(requestMapper).toQuiz(quizWebRequest);
  }

  @Test
  public void testUpdateQuizPassQuizWebRequestAndId() throws Exception {
    mockMvc.perform(
            put("/api/scoring/quiz/" + QUIZ_ID)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(QUIZ_UPDATE_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    dataResponseJacksonTester.write(DATA_RESPONSE)
                    .getJson()
            ));

    verify(quizService).updateQuiz(quiz);
    verify(requestMapper).toQuiz(QUIZ_ID, quizWebRequest);
  }
}