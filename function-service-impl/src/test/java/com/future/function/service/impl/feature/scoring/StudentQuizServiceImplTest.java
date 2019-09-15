package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import java.util.Observable;
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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentQuizServiceImplTest {

  private static final String USER_ID = "student-id";

  private static final String USER_NAME = "student";

  private static final String QUIZ_ID = "quiz-id";

  private static final Integer QUIZ_TRIALS = 3;

  private static final String STUDENT_QUIZ_ID = "student-quiz-id";

  private static final String BATCH_CODE = "batch-code";

  private static final String TARGET_BATCH = "target-batch";

  private static final String STUDENT_QUIZ_DETAIL_ID = "student-detail-quiz-id";

  private static final String STUDENT_QUESTION_ID = "student-question-id";

  private static final String QUESTION_ID = "question-id";

  private static final String OPTION_ID = "option-id";

  private static final String OPTION_LABEL = "option-label";

  private static final String QUESTION_TEXT = "question-text";

  private Batch batch;

  private User student;

  private Quiz quiz;

  private StudentQuiz studentQuiz;

  private StudentQuizDetail studentQuizDetail;

  private StudentQuestion studentQuestion;

  private Question question;

  private Option option;

  private Pageable pageable;

  @InjectMocks
  private StudentQuizServiceImpl studentQuizService;

  @Mock
  private StudentQuizRepository studentQuizRepository;

  @Mock
  private UserService userService;

  @Mock
  private BatchService batchService;

  @Mock
  private QuizService quizService;

  @Mock
  private StudentQuizDetailService studentQuizDetailService;


  @Before
  public void setUp() throws Exception {

    question = Question.builder()
      .id(QUESTION_ID)
      .label(QUESTION_TEXT)
      .build();

    option = Option.builder()
      .id(OPTION_ID)
      .label(OPTION_LABEL)
      .correct(true)
      .question(question)
      .build();

    student = User.builder()
      .id(USER_ID)
      .name(USER_NAME)
      .role(Role.STUDENT)
      .batch(batch)
      .build();

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();

    quiz = Quiz.builder()
      .id(QUIZ_ID)
      .trials(QUIZ_TRIALS + 1)
      .batch(batch)
      .build();

    studentQuiz = StudentQuiz.builder()
      .id(STUDENT_QUIZ_ID)
      .quiz(quiz)
      .student(student)
      .trials(QUIZ_TRIALS)
      .build();

    studentQuizDetail = StudentQuizDetail.builder()
      .id(STUDENT_QUIZ_DETAIL_ID)
      .studentQuiz(studentQuiz)
      .point(100)
      .build();

    studentQuestion = StudentQuestion.builder()
      .id(STUDENT_QUESTION_ID)
      .number(1)
      .studentQuizDetail(studentQuizDetail)
      .question(question)
      .option(option)
      .build();

    pageable = new PageRequest(0, 10);

    when(studentQuizRepository.findAllByStudentIdAndDeletedFalse(USER_ID))
        .thenReturn(Collections.singletonList(studentQuiz));
    when(studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID))
        .thenReturn(Optional.of(studentQuiz));
    when(studentQuizRepository.findAllByStudentIdAndDeletedFalse(
      USER_ID)).thenReturn(Collections.singletonList(studentQuiz));
    when(studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(USER_ID,
                                                                       QUIZ_ID
    )).thenReturn(Optional.of(studentQuiz));
    when(studentQuizRepository.findByIdAndDeletedFalse(
      STUDENT_QUIZ_ID)).thenReturn(Optional.of(studentQuiz));
    when(studentQuizRepository.save(studentQuiz)).thenReturn(studentQuiz);
    when(studentQuizRepository.save(any(StudentQuiz.class))).thenReturn(
      studentQuiz);
    when(userService.getStudentsByBatchCode(BATCH_CODE)).thenReturn(
      Collections.singletonList(student));
    when(userService.getStudentsByBatchCode(TARGET_BATCH)).thenReturn(
      Collections.singletonList(student));
    when(userService.getUser(USER_ID)).thenReturn(student);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
    when(batchService.getBatchByCode(TARGET_BATCH)).thenReturn(batch);
    when(studentQuizDetailService.findAllUnansweredQuestionsByStudentQuizId(
      STUDENT_QUIZ_ID)).thenReturn(Collections.singletonList(studentQuestion));
    when(studentQuizDetailService.answerStudentQuiz(STUDENT_QUIZ_ID,
                                                    Collections.singletonList(
                                                      studentQuestion)
    )).thenReturn(studentQuizDetail);
    when(studentQuizDetailService.findLatestByStudentQuizId(
      STUDENT_QUIZ_ID)).thenReturn(studentQuizDetail);
    when(quizService.findById(QUIZ_ID)).thenReturn(quiz);
    verify(quizService).addObserver(studentQuizService);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(studentQuizRepository, batchService, quizService,
                             studentQuizDetailService, userService
    );
  }

  @Test
  public void findAllByStudentId() {

    List<StudentQuiz> actual = studentQuizService.findAllByStudentId(USER_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getId()).isEqualTo(STUDENT_QUIZ_ID);
    assertThat(actual.get(0).getTrials()).isEqualTo(QUIZ_TRIALS);
    verify(userService).getUser(USER_ID);
    verify(studentQuizRepository).findAllByStudentIdAndDeletedFalse(USER_ID);
  }

  @Test
  public void findAllByStudentIdNoPageable() {

    List<StudentQuizDetail> actual = studentQuizService.findAllQuizByStudentId(
      USER_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getStudentQuiz()
                 .getId()).isEqualTo(STUDENT_QUIZ_ID);
    assertThat(actual.get(0)
                 .getPoint()).isEqualTo(studentQuizDetail.getPoint());
    verify(studentQuizRepository).findAllByStudentIdAndDeletedFalse(USER_ID);
    verify(studentQuizDetailService).findLatestByStudentQuizId(STUDENT_QUIZ_ID);
  }

  @Test
  public void findAllByStudentIdEmptyStudentQuiz() {

    when(studentQuizRepository.findAllByStudentIdAndDeletedFalse(
      USER_ID)).thenReturn(null);
    List<StudentQuizDetail> actual = studentQuizService.findAllQuizByStudentId(
      USER_ID);
    assertThat(actual.size()).isEqualTo(0);
    verify(studentQuizRepository).findAllByStudentIdAndDeletedFalse(USER_ID);
  }

  @Test
  public void findByIdExist() {

    StudentQuiz studentQuiz = studentQuizService.findOrCreateByStudentIdAndQuizId(
      USER_ID, QUIZ_ID);
    assertThat(studentQuiz.getId()).isEqualTo(STUDENT_QUIZ_ID);
    assertThat(studentQuiz.getTrials()).isEqualTo(QUIZ_TRIALS);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID);
  }

  @Test
  public void findByIdNotExistAndCreateNew() {

    when(studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID)).thenReturn(
      Optional.empty());
    StudentQuiz studentQuiz = studentQuizService.findOrCreateByStudentIdAndQuizId(USER_ID, QUIZ_ID);
    assertThat(studentQuiz.getId()).isEqualTo(STUDENT_QUIZ_ID);
    assertThat(studentQuiz.getTrials()).isEqualTo(QUIZ_TRIALS);
    verify(quizService).findById(QUIZ_ID);
    verify(userService).getUser(USER_ID);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID);
    verify(studentQuizRepository).save(any(StudentQuiz.class));
  }

  @Test
  public void findAllUnansweredQuestionsByStudentQuizId() {

    List<StudentQuestion> actual =
      studentQuizService.findAllUnansweredQuestionByStudentQuizId(
        USER_ID, QUIZ_ID);
    assertThat(actual.size()).isEqualTo(1);
    verify(studentQuizDetailService).findAllUnansweredQuestionsByStudentQuizId(
      STUDENT_QUIZ_ID);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID);
    studentQuiz.setTrials(QUIZ_TRIALS - 1);
    verify(studentQuizRepository).save(studentQuiz);
  }

  @Test
  public void findAllUnansweredQuestionsByStudentQuizIdAndTrialEqualZero() {

    quiz.setTrials(QUIZ_TRIALS);
    catchException(
      () -> studentQuizService.findAllUnansweredQuestionByStudentQuizId(
        USER_ID, QUIZ_ID));
    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID);
  }

  @Test
  public void answerQuestionsByStudentQuizId() {

    StudentQuizDetail actual =
      studentQuizService.answerQuestionsByStudentQuizId(USER_ID, QUIZ_ID,
                                                        Collections.singletonList(
                                                          studentQuestion)
      );
    assertThat(actual.getStudentQuiz()
                 .getId()).isEqualTo(STUDENT_QUIZ_ID);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID);
    verify(studentQuizDetailService).answerStudentQuiz(
      STUDENT_QUIZ_ID, Collections.singletonList(studentQuestion));
  }

  @Test
  public void deleteById() {

    studentQuizService.deleteById(STUDENT_QUIZ_ID);
    studentQuiz.setDeleted(true);
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
    verify(studentQuizRepository).save(studentQuiz);
    verify(studentQuizDetailService).deleteByStudentQuiz(studentQuiz);
  }

  @Test
  public void deleteByBatchCodeAndQuiz() {

    studentQuiz.setDeleted(true);
    studentQuizService.deleteByBatchCodeAndQuiz(quiz);
    verify(userService).getStudentsByBatchCode(BATCH_CODE);
    verify(studentQuizDetailService).deleteByStudentQuiz(studentQuiz);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(
      USER_ID, QUIZ_ID);
    verify(studentQuizRepository).save(studentQuiz);
  }

  @Test
  public void updateObserverSendQuizObjectTest() {

    studentQuizService.update(new Observable(), quiz);
    verify(userService).getStudentsByBatchCode(BATCH_CODE);
    verify(studentQuizRepository).findByStudentIdAndQuizIdAndDeletedFalse(USER_ID, QUIZ_ID);
    verify(studentQuizDetailService).deleteByStudentQuiz(studentQuiz);
    verify(studentQuizRepository).save(studentQuiz);
  }

  @Test
  public void updateObserverSendRandomObjectTest() {
    studentQuizService.update(new Observable(), new Object());
  }
}
