package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
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
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
  private Page<StudentQuiz> studentQuizPage;

  @InjectMocks
  private StudentQuizServiceImpl studentQuizService;

  @Mock
  private StudentQuizRepository studentQuizRepository;

  @Mock
  private UserService userService;

  @Mock
  private BatchService batchService;

  @Mock
  private StudentQuizDetailService studentQuizDetailService;


  @Before
  public void setUp() throws Exception {

    question = Question
        .builder()
        .id(QUESTION_ID)
        .label(QUESTION_TEXT)
        .build();

    option = Option
        .builder()
        .id(OPTION_ID)
        .label(OPTION_LABEL)
        .correct(true)
        .question(question)
        .build();

    student = User
        .builder()
        .id(USER_ID)
        .name(USER_NAME)
        .role(Role.STUDENT)
        .build();

    batch = Batch
        .builder()
        .code(BATCH_CODE)
        .build();

    quiz = Quiz
        .builder()
        .id(QUIZ_ID)
        .trials(QUIZ_TRIALS)
        .batch(batch)
        .build();

    studentQuiz = StudentQuiz
        .builder()
        .id(STUDENT_QUIZ_ID)
        .quiz(quiz)
        .student(student)
        .trials(QUIZ_TRIALS)
        .build();

    studentQuizDetail = StudentQuizDetail
        .builder()
        .id(STUDENT_QUIZ_DETAIL_ID)
        .studentQuiz(studentQuiz)
        .build();

    studentQuestion = StudentQuestion
        .builder()
        .id(STUDENT_QUESTION_ID)
        .number(1)
        .studentQuizDetail(studentQuizDetail)
        .question(question)
        .option(option)
        .build();

    pageable = new PageRequest(0, 10);

    studentQuizPage = new PageImpl<>(Collections.singletonList(studentQuiz), pageable, 1);

    when(studentQuizRepository.findAllByStudentId(USER_ID, pageable))
        .thenReturn(studentQuizPage);
    when(studentQuizRepository.findByStudentIdAndQuizId(USER_ID, QUIZ_ID))
        .thenReturn(Optional.of(studentQuiz));
    when(studentQuizRepository.findByIdAndDeletedFalse(STUDENT_QUIZ_ID))
        .thenReturn(Optional.of(studentQuiz));
    when(studentQuizRepository.save(studentQuiz))
        .thenReturn(studentQuiz);
    when(studentQuizRepository.save(any(StudentQuiz.class)))
        .thenReturn(studentQuiz);
    when(userService.getStudentsByBatchCode(BATCH_CODE))
        .thenReturn(Collections.singletonList(student));
    when(userService.getStudentsByBatchCode(TARGET_BATCH))
        .thenReturn(Collections.singletonList(student));
    when(userService.getUser(USER_ID)).thenReturn(student);
    when(batchService.getBatchByCode(BATCH_CODE))
        .thenReturn(batch);
    when(batchService.getBatchByCode(TARGET_BATCH))
        .thenReturn(batch);
    when(studentQuizDetailService.createStudentQuizDetail(studentQuiz, null))
        .thenReturn(new StudentQuizDetail());
    when(studentQuizDetailService.findAllUnansweredQuestionsByStudentQuizId(STUDENT_QUIZ_ID))
        .thenReturn(Collections.singletonList(studentQuestion));
    when(studentQuizDetailService.findAllQuestionsByStudentQuizId(STUDENT_QUIZ_ID))
        .thenReturn(Collections.singletonList(studentQuestion));
    when(studentQuizDetailService.answerStudentQuiz(STUDENT_QUIZ_ID, Collections.singletonList(studentQuestion)))
        .thenReturn(studentQuizDetail);

  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(studentQuizRepository, batchService, studentQuizDetailService,
        userService);
  }

  @Test
  public void findAllByStudentId() {
    Page<StudentQuiz> actual = studentQuizService.findAllByStudentId(USER_ID, pageable, USER_ID);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent().get(0).getId()).isEqualTo(STUDENT_QUIZ_ID);
    assertThat(actual.getContent().get(0).getTrials()).isEqualTo(QUIZ_TRIALS);
    verify(userService).getUser(USER_ID);
    verify(studentQuizRepository).findAllByStudentId(USER_ID, pageable);
  }

  @Test
  public void findAllByStudentIdAccessedByAnotherStudent() {
    when(userService.getUser("student-2")).thenReturn(User.builder().id("student-2").role(Role.STUDENT).build());
    catchException(() -> studentQuizService.findAllByStudentId(USER_ID, pageable, "student-2"));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(userService).getUser("student-2");
  }

  @Test
  public void findById() {
    StudentQuiz studentQuiz = studentQuizService.findById(STUDENT_QUIZ_ID, USER_ID);
    assertThat(studentQuiz.getId()).isEqualTo(STUDENT_QUIZ_ID);
    assertThat(studentQuiz.getTrials()).isEqualTo(QUIZ_TRIALS);
    verify(userService).getUser(USER_ID);
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
  }

  @Test
  public void findByIdAnotherStudentAndThrowForbiddenException() {
    when(userService.getUser("student-2")).thenReturn(User.builder().id("student-2").role(Role.STUDENT).build());
    catchException(() -> studentQuizService.findById(STUDENT_QUIZ_ID, "student-2"));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(userService).getUser("student-2");
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
  }

  @Test
  public void findAllQuestionsByStudentQuizId() {
    List<StudentQuestion> actual = studentQuizService.findAllQuestionsByStudentQuizId(STUDENT_QUIZ_ID, USER_ID);
    assertThat(actual.size()).isEqualTo(1);
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
    verify(studentQuizDetailService).findAllQuestionsByStudentQuizId(STUDENT_QUIZ_ID);
  }

  @Test
  public void findAllUnansweredQuestionsByStudentQuizId() {
    List<StudentQuestion> actual = studentQuizService.findAllUnansweredQuestionByStudentQuizId(STUDENT_QUIZ_ID, USER_ID);
    assertThat(actual.size()).isEqualTo(1);
    verify(studentQuizDetailService).findAllUnansweredQuestionsByStudentQuizId(STUDENT_QUIZ_ID);
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
    studentQuiz.setTrials(QUIZ_TRIALS - 1);
    verify(userService).getUser(USER_ID);
    verify(studentQuizRepository).save(studentQuiz);
  }

  @Test
  public void answerQuestionsByStudentQuizId() {
    StudentQuizDetail actual = studentQuizService.answerQuestionsByStudentQuizId(STUDENT_QUIZ_ID, USER_ID,
        Collections.singletonList(studentQuestion));
    assertThat(actual.getStudentQuiz().getId()).isEqualTo(STUDENT_QUIZ_ID);
    verify(userService, times(2)).getUser(USER_ID);
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
    verify(studentQuizDetailService).answerStudentQuiz(STUDENT_QUIZ_ID, Collections.singletonList(studentQuestion));
  }

  @Test
  public void createStudentQuizAndSave() {
    StudentQuiz actual = studentQuizService.createStudentQuizAndSave(USER_ID, quiz);
    assertThat(actual.getQuiz().getId()).isEqualTo(QUIZ_ID);
    assertThat(actual.getQuiz().getTrials()).isEqualTo(QUIZ_TRIALS);
    assertThat(actual.getStudent().getId()).isEqualTo(USER_ID);
    assertThat(actual.getStudent().getName()).isEqualTo(USER_NAME);
    verify(userService).getUser(USER_ID);
    verify(studentQuizRepository).save(any(StudentQuiz.class));
  }

  @Test
  public void createStudentQuizByBatchCode() {
    Quiz actual = studentQuizService.createStudentQuizByBatchCode(BATCH_CODE, quiz);
    assertThat(actual.getBatch().getCode()).isEqualTo(BATCH_CODE);
    verify(userService).getStudentsByBatchCode(BATCH_CODE);
    verify(userService).getUser(USER_ID);
    verify(studentQuizDetailService).createStudentQuizDetail(studentQuiz, null);
    verify(studentQuizRepository).save(any(StudentQuiz.class));
  }

  @Test
  public void createStudentQuizByBatchCodeStudentQuizFailToSave() {
    when(userService.getStudentsByBatchCode(BATCH_CODE)).thenReturn(Collections.emptyList());
    catchException(() -> studentQuizService.createStudentQuizByBatchCode(BATCH_CODE, quiz));
    assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
    verify(userService).getStudentsByBatchCode(BATCH_CODE);
  }

  @Test
  public void copyQuizFromBatch() {
    Batch targetBatch = Batch.builder().code(TARGET_BATCH).build();
    Quiz actual = studentQuizService.copyQuizWithTargetBatch(targetBatch, quiz);
    assertThat(actual.getTrials()).isEqualTo(QUIZ_TRIALS);
    assertThat(actual.getId()).isNotEqualTo(QUIZ_ID);
    verify(studentQuizRepository).save(any(StudentQuiz.class));
    verify(userService).getStudentsByBatchCode(TARGET_BATCH);
    verify(userService).getUser(USER_ID);
    verify(studentQuizDetailService).createStudentQuizDetail(studentQuiz, null);
  }

  @Test
  public void deleteById() {
    studentQuizService.deleteById(STUDENT_QUIZ_ID);
    studentQuiz.setDeleted(true);
    verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
    verify(studentQuizRepository).save(studentQuiz);
  }

  @Test
  public void deleteByBatchCodeAndQuiz() {
    studentQuiz.setDeleted(true);
    studentQuizService.deleteByBatchCodeAndQuiz(BATCH_CODE, QUIZ_ID);
    verify(userService).getStudentsByBatchCode(BATCH_CODE);
    verify(studentQuizDetailService).deleteByStudentQuiz(studentQuiz);
    verify(studentQuizRepository).findByStudentIdAndQuizId(USER_ID, QUIZ_ID);
    verify(studentQuizRepository).save(studentQuiz);
  }
}
