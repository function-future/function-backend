package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.repository.feature.scoring.QuizRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceImplTest {

  private String QUIZ_ID = UUID.randomUUID().toString();
  private String QUIZ_TITLE = "quiz-title";
  private String QUIZ_DESCRIPTION = "quiz-description";
  private long DATE = 0;
  private long TIME_LIMIT = 0;
  private int TRIALS = 0;
  private int QUESTION_COUNT = 0;

  private int PAGE = 0;
  private int TOTAL = 10;

  private static final String NOT_FOUND_MSG = "Quiz Not Found";

  private Quiz quiz;
  private Pageable pageable;
  private List<Quiz> quizList;
  private Page<Quiz> quizPage;

  @InjectMocks
  private QuizServiceImpl quizService;

  @Mock
  private QuizRepository quizRepository;

  @Before
  public void setUp() throws Exception {
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
            .build();

    quizList = Collections.singletonList(quiz);

    pageable = new PageRequest(PAGE, TOTAL);

    quizPage = new PageImpl<>(quizList, pageable, TOTAL);

    when(quizRepository.findAll(pageable)).thenReturn(quizPage);
    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(Optional.of(quiz));
    when(quizRepository.save(quiz)).thenReturn(quiz);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(quizRepository);
  }

  @Test
  public void testFindQuizById() {
    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(Optional.of(quiz));
    Quiz actual = quizService.findById(QUIZ_ID);
    assertThat(actual).isNotNull();
    assertThat(actual).isEqualTo(quiz);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
  }

  @Test
  public void testFindQuizByIdAndDeletedFalseNull() {
    catchException(() -> quizService.findById(null));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(NOT_FOUND_MSG);
  }

  @Test
  public void testFindQuizByIdAndDeletedFalseBlank() {
    catchException(() -> quizService.findById(""));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(NOT_FOUND_MSG);
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterAndSearch() {
    Page<Quiz> actual = quizService.findAllByPageableAndFilterAndSearch(pageable, "", "");
    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAll(eq(pageable));
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterNullAndSearchNull() {
    Page<Quiz> actual = quizService.findAllByPageableAndFilterAndSearch(pageable, null, null);

    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAll(eq(pageable));
  }

  @Test
  public void testCreateQuizSuccess() {
    Quiz actual = quizService.createQuiz(quiz);
    assertThat(actual).isEqualTo(quiz);

    verify(quizRepository).save(eq(quiz));
  }

  @Test
  public void testCreateQuizFail() {
    when(quizRepository.save(quiz)).thenThrow(BadRequestException.class);
    catchException(() -> quizService.createQuiz(quiz));

    verify(quizRepository).save(quiz);
    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
  }

  @Test
  public void testUpdateQuizSuccess() {
    quiz.setId(QUIZ_ID);
    Quiz actual = quizService.updateQuiz(quiz);
    assertThat(actual).isEqualTo(quiz);

    verify(quizRepository).findByIdAndDeletedFalse(eq(QUIZ_ID));
    verify(quizRepository).save(eq(quiz));
  }

  @Test
  public void testUpdateQuizFindByIdNotFound() {
    quiz.setId("randomId");
    catchException(() -> quizService.updateQuiz(quiz));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(NOT_FOUND_MSG);

    verify(quizRepository).findByIdAndDeletedFalse("randomId");
  }

  @Test
  public void testDeleteQuizSuccess() {
    quizService.deleteById(QUIZ_ID);

    quiz.setDeleted(true);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
    verify(quizRepository).save(quiz);
  }
}