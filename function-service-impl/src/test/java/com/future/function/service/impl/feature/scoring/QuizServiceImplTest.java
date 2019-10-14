package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuestionService;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceImplTest {

  private static final String QUIZ_ID = UUID.randomUUID()
    .toString();

  private static final String QUIZ_TITLE = "quiz-title";

  private static final String QUIZ_DESCRIPTION = "quiz-description";

  private static final long DATE = 15000000;

  private static final long TIME_LIMIT = 1500000;

  private static final int TRIALS = 10;

  private static final int QUESTION_COUNT = 10;

  private static final String BATCH_CODE = "batch-code";

  private static final String BATCH_ID = "batch-id";

  private static final String QUESTION_BANK_ID = "question-bank-id";

  private static final String QUESTION_BANK_DESCRIPTION =
    "question-bank-description";

  private static final String QUESTION_TEXT = "question-text";

  private int PAGE = 0;

  private int TOTAL = 10;

  private Quiz quiz;

  private Batch batch;

  private QuestionBank questionBank;

  private Question question;

  private Pageable pageable;

  private List<Quiz> quizList;

  private Page<Quiz> quizPage;

  @InjectMocks
  private QuizServiceImpl quizService;

  @Mock
  private QuizRepository quizRepository;

  @Mock
  private QuestionService questionService;

  @Mock
  private BatchService batchService;

  @Mock
  private QuestionBankService questionBankService;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .id(BATCH_ID)
      .build();

    questionBank = QuestionBank.builder()
      .id(QUESTION_BANK_ID)
      .description(QUESTION_BANK_DESCRIPTION)
      .build();

    question = Question.builder()
      .questionBank(questionBank)
      .label(QUESTION_TEXT)
      .build();

    quiz = Quiz.builder()
      .id(QUIZ_ID)
      .title(QUIZ_TITLE)
      .description(QUIZ_DESCRIPTION)
      .startDate(DATE)
      .endDate(DATE)
      .timeLimit(TIME_LIMIT)
      .trials(TRIALS)
      .questionCount(QUESTION_COUNT)
      .questionBanks(Collections.singletonList(questionBank))
      .batch(batch)
      .build();

    quizList = Collections.singletonList(quiz);

    pageable = new PageRequest(PAGE, TOTAL);

    quizPage = new PageImpl<>(quizList, pageable, TOTAL);

    when(
      quizRepository.findAllByBatchAndDeletedFalseOrderByEndDateAsc(batch, pageable)).thenReturn(
      quizPage);
    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(
      Optional.of(quiz));
    when(quizRepository.save(quiz)).thenReturn(quiz);
    when(questionService.findAllByMultipleQuestionBankId(
      Collections.singletonList(QUESTION_BANK_ID))).thenReturn(
      Collections.singletonList(question));
    when(questionBankService.findById(QUESTION_BANK_ID)).thenReturn(
      questionBank);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(quizRepository, batchService, questionService, batchService, questionBankService);
  }

  @Test
  public void testFindQuizById() {

    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(
      Optional.of(quiz));
    Quiz actual = quizService.findById(QUIZ_ID, Role.ADMIN, "");
    assertThat(actual).isNotNull();
    assertThat(actual).isEqualTo(quiz);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
  }

  @Test
  public void testFindQuizByIdAccessedByStudent() {

    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(
        Optional.of(quiz));
    Quiz actual = quizService.findById(QUIZ_ID, Role.STUDENT, BATCH_ID);
    assertThat(actual).isNotNull();
    assertThat(actual).isEqualTo(quiz);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
  }

  @Test
  public void testFindQuizByIdAccessedByAnotherStudentBatch() {

    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(
        Optional.of(quiz));
    catchException(() -> quizService
        .findById(QUIZ_ID, Role.STUDENT, "another-batch-id"));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
  }

  @Test
  public void testFindQuizByIdAndDeletedFalseNull() {

    catchException(() -> quizService.findById(null, Role.ADMIN, ""));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void testFindQuizByIdAndDeletedFalseBlank() {

    when(quizRepository.findByIdAndDeletedFalse("")).thenReturn(
      Optional.empty());
    catchException(() -> quizService.findById("", Role.ADMIN, ""));
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    verify(quizRepository).findByIdAndDeletedFalse("");
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterAndSearch() {

    Page<Quiz> actual = quizService.findAllByBatchCodeAndPageable(
      BATCH_CODE, pageable, Role.STUDENT, BATCH_ID);
    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAllByBatchAndDeletedFalseOrderByEndDateAsc(batch, pageable);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterAndSearchAndAccessedByAdmin() {

    Page<Quiz> actual = quizService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.ADMIN, "");
    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAllByBatchAndDeletedFalseOrderByEndDateAsc(batch, pageable);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterNullAndSearchNull() {

    Page<Quiz> actual = quizService.findAllByBatchCodeAndPageable(
      BATCH_CODE, pageable, Role.STUDENT, BATCH_ID);

    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAllByBatchAndDeletedFalseOrderByEndDateAsc(batch, pageable);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterNullAndSearchNullAndAnotherStudentBatchAccess() {

    catchException(() -> quizService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.STUDENT, "another-batch-id"));

    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);

    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testCreateQuizSuccess() {

    Quiz actual = quizService.createQuiz(quiz);
    assertThat(actual).isEqualTo(quiz);

    verify(quizRepository).save(eq(quiz));
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(questionBankService).findById(QUESTION_BANK_ID);
  }

  @Test
  public void testCreateQuizAllQuestionBankSuccess() {

    quiz.setQuestionBanks(Collections.singletonList(QuestionBank.builder()
                                                      .id("ALL")
                                                      .build()));
    Quiz actual = quizService.createQuiz(quiz);
    assertThat(actual).isEqualTo(quiz);

    verify(quizRepository).save(eq(quiz));
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(questionBankService).findAll();
  }

  @Test
  public void testCreateQuizFail() {

    when(quizRepository.save(quiz)).thenThrow(BadRequestException.class);
    catchException(() -> quizService.createQuiz(quiz));
    verify(quizRepository).save(quiz);
    verify(questionBankService).findById(QUESTION_BANK_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
  }

  @Test
  public void testUpdateQuizSuccess() {

    quiz.setId(QUIZ_ID);
    Quiz actual = quizService.updateQuiz(quiz);
    assertThat(actual).isEqualTo(quiz);

    verify(quizRepository).findByIdAndDeletedFalse(eq(QUIZ_ID));
    verify(quizRepository).save(eq(quiz));
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(questionBankService).findById(QUESTION_BANK_ID);
  }

  @Test
  public void testUpdateQuizFindByIdNotFound() {

    quiz.setId("randomId");
    when(quizRepository.findByIdAndDeletedFalse("randomId")).thenReturn(
      Optional.empty());
    Quiz actual = quizService.updateQuiz(quiz);
    assertThat(actual).isEqualTo(quiz);
    verify(quizRepository).findByIdAndDeletedFalse("randomId");
  }

  @Test
  public void testDeleteQuizSuccess() {

    quizService.deleteById(QUIZ_ID);

    quiz.setDeleted(true);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
    verify(quizRepository).save(quiz);
  }

  @Test
  public void copyQuizWithTargetBatch() {

    String batchCode = "ABC";
    Batch targetBatchObj = Batch.builder()
      .code(batchCode)
      .build();
    when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);
    when(batchService.getBatchByCode(batchCode)).thenReturn(targetBatchObj);
    Quiz actual = quizService.copyQuizWithTargetBatchCode(batchCode, QUIZ_ID);
    assertThat(actual.getTitle()).isEqualTo(QUIZ_TITLE);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
    verify(quizRepository).save(any(Quiz.class));
    verify(batchService).getBatchByCode(batchCode);
  }

}
