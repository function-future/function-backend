package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuizResponseMapperTest {

  private static String QUIZ_ID;
  private static final String QUIZ_TITLE = "quiz-title";
  private static final String QUIZ_DESCRIPTION = "quiz-description";
  private static final long DATE = 150000;
  private static final long QUIZ_TIME_LIMIT = 15000;
  private static final int QUIZ_QUESTION_COUNT = 2;
  private static final int QUIZ_TRIALS = 3;

  private static final int PAGE = 0;
  private static final int SIZE = 10;

  private Quiz quiz;
  private Paging paging;
  private Pageable pageable;
  private List<Quiz> quizList;
  private Page<Quiz> quizPage;
  private QuizWebResponse quizWebResponse;
  private List<QuizWebResponse> quizWebResponseList;
  private DataResponse<QuizWebResponse> quizWebDataResponse;
  private PagingResponse<QuizWebResponse> quizWebPagingResponse;

  @Before
  public void setUp() throws Exception {
    quiz = Quiz
            .builder()
            .title(QUIZ_TITLE)
            .description(QUIZ_DESCRIPTION)
            .startDate(DATE)
            .endDate(DATE)
            .timeLimit(QUIZ_TIME_LIMIT)
            .questionCount(QUIZ_QUESTION_COUNT)
            .trials(QUIZ_TRIALS)
            .build();

    quizList = new ArrayList<>();
    quizList.add(quiz);

    quizWebResponse = new QuizWebResponse();
    BeanUtils.copyProperties(quiz, quizWebResponse);

    quizWebDataResponse = DataResponse
            .<QuizWebResponse> builder()
            .data(quizWebResponse)
            .build();

    quizWebResponseList = new ArrayList<>();
    quizWebResponseList.add(quizWebResponse);

    paging = Paging
            .builder()
            .totalRecords(SIZE)
            .size(SIZE)
            .page(PAGE)
            .build();

    quizWebPagingResponse = PagingResponse
            .<QuizWebResponse> builder()
            .data(quizWebResponseList)
            .paging(paging)
            .code(HttpStatus.OK.value())
            .build();

    pageable = new PageRequest(PAGE, SIZE);

    quizPage = new PageImpl<>(quizList, pageable, SIZE);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testToQuizWebResponseWithHttpStatusCreated() {
    quizWebDataResponse.setCode(HttpStatus.CREATED.value());
    DataResponse<QuizWebResponse> actual = QuizResponseMapper.toQuizWebDataResponse(HttpStatus.CREATED, quiz);
    assertThat(actual.getCode()).isEqualTo(quizWebDataResponse.getCode());
    assertThat(actual.getData()).isEqualTo(quizWebResponse);
  }

  @Test
  public void testToQuizWebResponse() {
    quizWebDataResponse.setCode(HttpStatus.OK.value());
    DataResponse<QuizWebResponse> actual = QuizResponseMapper.toQuizWebDataResponse(quiz);
    assertThat(actual.getCode()).isEqualTo(quizWebDataResponse.getCode());
    assertThat(actual.getData()).isEqualTo(quizWebResponse);
  }

  @Test
  public void testToPagingQuizWebResponse() {
    PagingResponse<QuizWebResponse> actual = QuizResponseMapper.toQuizWebPagingResponse(quizPage);
    assertThat(actual.getPaging()).isEqualTo(paging);
    assertThat(actual.getData()).isEqualTo(quizWebResponseList);
    assertThat(actual.getCode()).isEqualTo(quizWebPagingResponse.getCode());
  }
}