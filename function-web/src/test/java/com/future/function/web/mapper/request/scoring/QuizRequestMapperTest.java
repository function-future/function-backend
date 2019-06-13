package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.CopyQuizWebRequest;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuizRequestMapperTest {

  private String QUIZ_ID = UUID.randomUUID().toString();
  private String QUIZ_TITLE = "quiz-title";
  private String QUIZ_DESCRIPTION = "quiz-description";
  private long DATE = 0;
  private long TIME_LIMIT = 0;
  private int TRIALS = 0;
  private int QUESTION_COUNT = 0;
    private String QUESTION_BANK_ID = "question-bank-id";

  private Quiz quiz;
    private QuestionBank questionBank;
  private QuizWebRequest request;

  @InjectMocks
  private QuizRequestMapper requestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() throws Exception {

      questionBank = QuestionBank
              .builder()
              .id(QUESTION_BANK_ID)
              .build();

    quiz = Quiz
            .builder()
            .id(QUIZ_ID)
            .title(QUIZ_TITLE)
            .description(QUIZ_DESCRIPTION)
            .startDate(DATE)
            .endDate(DATE)
            .timeLimit(TIME_LIMIT)
            .trials(TRIALS)
            .questionCount(QUESTION_COUNT)
            .questionBanks(Collections.singletonList(questionBank))
            .build();

    request = new QuizWebRequest();
    BeanUtils.copyProperties(quiz, request);
      request.setQuestionBanks(Collections.singletonList(QUESTION_BANK_ID));

    when(validator.validate(request)).thenReturn(request);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testToQuizWithQuizWebRequest() {
    quiz.setId(null);
    Quiz actual = requestMapper.toQuiz(request);
      assertThat(actual.getTitle()).isEqualTo(quiz.getTitle());
    verify(validator).validate(request);
  }

  @Test
  public void testToQuizWithNullQuizWebRequest() {
    catchException(() -> requestMapper.toQuiz(null));
    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
  }

  @Test
  public void testToQuizWithIdAndQuizWebRequest() {
    Quiz actual = requestMapper.toQuiz(QUIZ_ID, request);
      assertThat(actual.getTitle()).isEqualTo(quiz.getTitle());
      verify(validator).validate(request);
  }

    @Test
    public void testValidateCopyQuizWebRequest() {
        CopyQuizWebRequest request = CopyQuizWebRequest
                .builder()
                .quizId(QUIZ_ID)
                .batchCode("3")
                .build();
        when(validator.validate(request)).thenReturn(request);
        CopyQuizWebRequest actual = requestMapper.validateCopyQuizWebRequest(request);
        assertThat(actual.getQuizId()).isEqualTo(QUIZ_ID);
        assertThat(actual.getBatchCode()).isEqualTo("3");
        verify(validator).validate(request);
    }

  @Test
  public void testToQuizWithIdNullAndQuizWebRequest() {
    catchException(() -> requestMapper.toQuiz(null, request));

    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
  }

  @Test
  public void testToQuizWithIdBlankAndQuizWebRequest() {
    catchException(() -> requestMapper.toQuiz("", request));

    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
  }
}