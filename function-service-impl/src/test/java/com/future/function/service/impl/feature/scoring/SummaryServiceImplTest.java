package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.model.enums.scoring.ScoringType;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SummaryServiceImplTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";

  private static final String QUIZ_TITLE = "quiz-title";

  private static final String STUDENT_ID = "student-id";

  private static final String USER_ID = "student-id";

  private static final String STUDENT_NAME = "studentName";

  private static final String BATCH_CODE = "batch-code";

  private static final String UNIVERSITY = "university";

  private static final String FILE_URL = "file-url";

  private static final int POINT = 100;

  private User student;

  private Room room;

  private FileV2 fileV2;

  private Batch batch;

  private SummaryVO quizSummaryVO;

  private SummaryVO roomSummaryVO;

  private StudentQuizDetail studentQuizDetail;

  @InjectMocks
  private SummaryServiceImpl summaryService;

  @Mock
  private RoomService roomService;

  @Mock
  private StudentQuizService studentQuizService;

  @Mock
  private UserService userService;

  @Before
  public void setUp() throws Exception {

    fileV2 = FileV2.builder()
      .fileUrl(FILE_URL)
      .build();
    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();
    student = User.builder()
      .id(STUDENT_ID)
      .name(STUDENT_NAME)
      .batch(batch)
      .university(UNIVERSITY)
      .pictureV2(fileV2)
      .role(Role.STUDENT)
      .build();
    room = Room.builder()
      .assignment(Assignment.builder()
                    .title(ASSIGNMENT_TITLE)
                    .build())
      .point(POINT)
      .build();
    studentQuizDetail = StudentQuizDetail.builder()
      .point(POINT)
      .studentQuiz(StudentQuiz.builder()
                     .quiz(Quiz.builder()
                             .title(QUIZ_TITLE)
                             .build())
                     .build())
      .build();

    when(roomService.findAllByStudentId(STUDENT_ID)).thenReturn(
      Collections.singletonList(room));
    when(studentQuizService.findAllQuizByStudentId(STUDENT_ID)).thenReturn(
      Collections.singletonList(studentQuizDetail));
    when(userService.getUser(USER_ID)).thenReturn(User.builder()
                                                    .id(USER_ID)
                                                    .role(Role.STUDENT)
                                                    .build());
    when(userService.getUser(STUDENT_ID)).thenReturn(student);

  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(roomService, studentQuizService, userService);
  }

  @Test
  public void findAllPointSummaryByStudentId() {

    StudentSummaryVO actual = summaryService.findAllPointSummaryByStudentId(
      STUDENT_ID, USER_ID);
    assertThat(actual.getStudentName()).isEqualTo(STUDENT_NAME);
    assertThat(actual.getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getUniversity()).isEqualTo(UNIVERSITY);
    assertThat(actual.getAvatar()).isEqualTo(FILE_URL);
    assertThat(actual.getScores()
                 .get(0)
                 .getTitle()).isEqualTo(ASSIGNMENT_TITLE);
    assertThat(actual.getScores()
                 .get(0)
                 .getType()).isEqualTo(ScoringType.ASSIGNMENT.getType());
    assertThat(actual.getScores()
                 .get(1)
                 .getTitle()).isEqualTo(QUIZ_TITLE);
    assertThat(actual.getScores()
                 .get(1)
                 .getType()).isEqualTo(ScoringType.QUIZ.getType());
    verify(roomService).findAllByStudentId(STUDENT_ID);
    verify(studentQuizService).findAllQuizByStudentId(STUDENT_ID);
    verify(userService, times(2)).getUser(USER_ID);
  }

  @Test
  public void findAllPointSummaryByStudentIdAccessedByAdmin() {

    User anotherUser = User.builder()
      .id("id")
      .role(Role.ADMIN)
      .build();
    when(userService.getUser("id")).thenReturn(anotherUser);
    StudentSummaryVO actual = summaryService.findAllPointSummaryByStudentId(
      STUDENT_ID, "id");
    assertThat(actual.getStudentName()).isEqualTo(STUDENT_NAME);
    assertThat(actual.getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getUniversity()).isEqualTo(UNIVERSITY);
    assertThat(actual.getAvatar()).isEqualTo(FILE_URL);
    assertThat(actual.getScores()
                 .get(0)
                 .getTitle()).isEqualTo(ASSIGNMENT_TITLE);
    assertThat(actual.getScores()
                 .get(0)
                 .getType()).isEqualTo(ScoringType.ASSIGNMENT.getType());
    assertThat(actual.getScores()
                 .get(1)
                 .getTitle()).isEqualTo(QUIZ_TITLE);
    assertThat(actual.getScores()
                 .get(1)
                 .getType()).isEqualTo(ScoringType.QUIZ.getType());
    verify(roomService).findAllByStudentId(STUDENT_ID);
    verify(studentQuizService).findAllQuizByStudentId(STUDENT_ID);
    verify(userService).getUser("id");
    verify(userService).getUser(STUDENT_ID);
  }

  @Test
  public void findAllPointSummaryByStudentIdUserIdNotEquals() {

    User anotherUser = User.builder()
      .id("id")
      .role(Role.STUDENT)
      .build();
    when(userService.getUser("id")).thenReturn(anotherUser);
    catchException(
      () -> summaryService.findAllPointSummaryByStudentId(STUDENT_ID, "id"));
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    verify(userService).getUser("id");
  }

}
