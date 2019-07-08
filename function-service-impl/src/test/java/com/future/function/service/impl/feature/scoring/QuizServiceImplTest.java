package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceImplTest {

  private static final String QUIZ_ID = UUID.randomUUID().toString();
  private static final String QUIZ_TITLE = "quiz-title";
  private static final String QUIZ_DESCRIPTION = "quiz-description";
  private static final long DATE = 15000000;
  private static final long TIME_LIMIT = 1500000;
  private static final int TRIALS = 10;
  private static final int QUESTION_COUNT = 10;

  private static final String BATCH_CODE = "batch-code";

  private static final String QUESTION_BANK_ID = "question-bank-id";
  private static final String QUESTION_BANK_DESCRIPTION = "question-bank-description";

  private static final String QUESTION_ID = "question-id";
  private static final String QUESTION_TEXT = "question-text";
  private static final String NOT_FOUND_MSG = "Quiz Not Found";
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
  private StudentQuizService studentQuizService;

  @Mock
  private QuestionService questionService;

  @Mock
  private BatchService batchService;

  @Mock
  private QuestionBankService questionBankService;

  @Before
  public void setUp() throws Exception {
    batch = Batch
        .builder()
        .code(BATCH_CODE)
        .build();

    questionBank = QuestionBank
        .builder()
        .id(QUESTION_BANK_ID)
        .description(QUESTION_BANK_DESCRIPTION)
        .build();

    question = Question
        .builder()
        .questionBank(questionBank)
        .text(QUESTION_TEXT)
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
        .batch(batch)
        .build();

    quizList = Collections.singletonList(quiz);

    pageable = new PageRequest(PAGE, TOTAL);

    quizPage = new PageImpl<>(quizList, pageable, TOTAL);

    when(quizRepository.findAllByDeletedFalse(pageable)).thenReturn(quizPage);
    when(quizRepository.findByIdAndDeletedFalse(QUIZ_ID)).thenReturn(Optional.of(quiz));
    when(quizRepository.save(quiz)).thenReturn(quiz);
    when(studentQuizService.createStudentQuizByBatchCode(BATCH_CODE, quiz))
        .thenReturn(quiz);
    when(questionService.findAllByMultipleQuestionBankId(Collections.singletonList(QUESTION_BANK_ID)))
        .thenReturn(Collections.singletonList(question));
    when(questionBankService.findById(QUESTION_BANK_ID)).thenReturn(questionBank);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
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
    Page<Quiz> actual = quizService.findAllByBatchCodeAndPageable(BATCH_CODE, pageable);
    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAllByDeletedFalse(pageable);
  }

  @Test
  public void testFindPageOfQuizWithPageableFilterNullAndSearchNull() {
    Page<Quiz> actual = quizService.findAllByBatchCodeAndPageable(BATCH_CODE, pageable);

    assertThat(actual.getContent()).isEqualTo(quizList);
    assertThat(actual.getTotalElements()).isEqualTo(TOTAL);
    assertThat(actual).isEqualTo(quizPage);

    verify(quizRepository).findAllByDeletedFalse(pageable);
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
    verify(questionBankService).findById(QUESTION_BANK_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
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
  public void testUpdateQuizSuccessChangeBatch() {
    Batch anotherBatch = Batch.builder().code("abc").build();
    Quiz request = Quiz.builder().id(QUIZ_ID).batch(anotherBatch).build();
    quiz.setId(QUIZ_ID);
    Quiz actual = quizService.updateQuiz(request);
    assertThat(actual).isEqualTo(quiz);

    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
    verify(quizRepository).save(quiz);
    verify(studentQuizService).deleteByBatchCodeAndQuiz(batch.getCode(), QUIZ_ID);
    verify(studentQuizService).createStudentQuizByBatchCode("abc", quiz);
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
    verify(studentQuizService).deleteByBatchCodeAndQuiz(BATCH_CODE, QUIZ_ID);
  }

  @Test
  public void copyQuizWithTargetBatch() {
    String targetBatch = "ABC";
    Batch targetBatchObj = Batch.builder().code(targetBatch).build();
    when(batchService.getBatchByCode(targetBatch)).thenReturn(targetBatchObj);
    when(studentQuizService.copyQuizWithTargetBatch(targetBatchObj, quiz)).thenReturn(quiz);
    Quiz actual = quizService.copyQuizWithTargetBatch(targetBatch, quiz);
    assertThat(actual.getTitle()).isEqualTo(QUIZ_TITLE);
    verify(quizRepository).findByIdAndDeletedFalse(QUIZ_ID);
    verify(studentQuizService).copyQuizWithTargetBatch(targetBatchObj, quiz);
    quiz.getBatch().setCode(targetBatch);
    verify(quizRepository).save(quiz);
  }
}