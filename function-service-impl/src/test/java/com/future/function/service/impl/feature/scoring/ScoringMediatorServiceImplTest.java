package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoringMediatorServiceImplTest {

  private static final String QUIZ_ID = "quiz-id";

  private static final String QUIZ_TITLE = "quiz-title";

  private static final String QUIZ_DESCRIPTION = "quiz-description";

  private static final int QUESTION_COUNT = 10;

  private static final int PAGE = 0;

  private static final int SIZE = Integer.MAX_VALUE;

  private static final String BATCH_CODE = "batch-code";

  private static final String ASSIGNMENT_ID = "assignment-id";

  private static final String ASSIGNMENT_TITLE = "assignment-title";

  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";

  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();

  private static final String BATCH_ID = "batch-id";

  private static final String USER_ID = "userId";

  private static final String USERNAME = "userName";

  private static final long DATE = 15000000;

  private static final long TIME_LIMIT = 1500000;

  private static final int TRIALS = 10;

  private static final String STUDENT_QUIZ_ID = "student-quiz-id";

  private static final String ROOM_ID = "room-id";

  private Quiz quiz;

  private User user;

  private Room room;

  private Batch batch;

  private Pageable pageable;

  private StudentQuiz studentQuiz;

  private Assignment assignment;

  @Mock
  private QuizService quizService;

  @Mock
  private StudentQuizService studentQuizService;

  @Mock
  private AssignmentService assignmentService;

  @Mock
  private RoomService roomService;

  @InjectMocks
  private ScoringMediatorServiceImpl mediatorService;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .id(BATCH_ID)
      .build();

    user = User.builder()
      .id(USER_ID)
      .name(USERNAME)
      .role(Role.STUDENT)
      .batch(batch)
      .build();

    assignment = Assignment.builder()
      .id(ASSIGNMENT_ID)
      .title(ASSIGNMENT_TITLE)
      .description(ASSIGNMENT_DESCRIPTION)
      .deadline(ASSIGNMENT_DEADLINE)
      .batch(batch)
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
      .batch(batch)
      .build();

    studentQuiz = StudentQuiz.builder()
      .id(STUDENT_QUIZ_ID)
      .build();

    room = Room.builder()
      .id(ROOM_ID)
      .build();

    pageable = new PageRequest(PAGE, SIZE);

    when(quizService.findAllByBatchCodeAndPageable(BATCH_CODE,
                                                   pageable
    )).thenReturn(new PageImpl<>(Collections.singletonList(quiz), pageable, 1));
    when(assignmentService.findAllByBatchCodeAndPageable(BATCH_CODE,
                                                         pageable
    )).thenReturn(
      new PageImpl<>(Collections.singletonList(assignment), pageable, 1));
    when(studentQuizService.findAllByStudentId(USER_ID, pageable,
                                               USER_ID
    )).thenReturn(
      new PageImpl<>(Collections.singletonList(studentQuiz), pageable, 1));
    when(roomService.findAllByStudentId(USER_ID)).thenReturn(
      Collections.singletonList(room));
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(
      quizService, assignmentService, studentQuizService, roomService);
  }

  @Test
  public void createQuizAndAssignmentsByStudent() {

    User actual = mediatorService.createQuizAndAssignmentsByStudent(user);
    assertThat(actual.getId()).isEqualTo(USER_ID);
    assertThat(actual.getName()).isEqualTo(USERNAME);
    verify(quizService).findAllByBatchCodeAndPageable(BATCH_CODE, pageable);
    verify(assignmentService).findAllByBatchCodeAndPageable(
      BATCH_CODE, pageable);
    verify(studentQuizService).createStudentQuizAndSave(user, quiz);
    verify(roomService).createRoomForUserAndSave(user, assignment);
  }

  @Test
  public void createQuizAndAssignmentsByStudentUserRoleIsNotStudent() {

    user.setRole(Role.ADMIN);
    User actual = mediatorService.createQuizAndAssignmentsByStudent(user);
    assertThat(actual.getId()).isEqualTo(USER_ID);
    assertThat(actual.getName()).isEqualTo(USERNAME);
  }

  @Test
  public void createQuizAndAssignmentsByStudentThrowException() {

    doThrow(Exception.class).when(studentQuizService)
      .createStudentQuizAndSave(user, quiz);
    User actual = mediatorService.createQuizAndAssignmentsByStudent(user);
    assertThat(actual.getId()).isEqualTo(USER_ID);
    assertThat(actual.getName()).isEqualTo(USERNAME);
    verify(quizService).findAllByBatchCodeAndPageable(BATCH_CODE, pageable);
    verify(assignmentService).findAllByBatchCodeAndPageable(
      BATCH_CODE, pageable);
    verify(studentQuizService).createStudentQuizAndSave(user, quiz);
    verify(roomService).createRoomForUserAndSave(user, assignment);
  }

  @Test
  public void deleteQuizAndAssignmentsByStudent() {

    User actual = mediatorService.deleteQuizAndAssignmentsByStudent(user);
    assertThat(actual.getId()).isEqualTo(USER_ID);
    assertThat(actual.getName()).isEqualTo(USERNAME);
    verify(studentQuizService).findAllByStudentId(USER_ID, pageable, USER_ID);
    verify(studentQuizService).deleteById(STUDENT_QUIZ_ID);
    verify(roomService).findAllByStudentId(USER_ID);
    verify(roomService).deleteRoomById(ROOM_ID);
  }

}
