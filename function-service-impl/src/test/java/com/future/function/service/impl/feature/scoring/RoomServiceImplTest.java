package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.RoomRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.impl.helper.PageHelper;
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
import java.util.Date;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceImplTest {

  private static final String ROOM_ID = "room-id";

  private static final String ASSIGNMENT_ID = "assignment-id";

  private static final long DEADLINE = 15000000;

  private static final String USER_ID = "userId";

  private static final String USERNAME = "userName";

  private static final String BATCH_CODE = "batchCode";

  private static final String BATCH_ID = "batchId";

  private static final String MENTOR_ID = "mentor-id";

  private Room room;

  private Assignment assignment;

  private Pageable pageable;

  private Page<Room> roomPage;

  private Comment comment;

  private Batch batch;

  private User student;

  @InjectMocks
  private RoomServiceImpl roomService;

  @Mock
  private CommentService commentService;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private UserService userService;

  @Mock
  private AssignmentService assignmentService;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .id(BATCH_ID)
      .build();
    assignment = Assignment.builder()
      .id(ASSIGNMENT_ID)
      .batch(batch)
      .deadline(new Date(2019, 11, 12).getTime())
      .build();
    student = User.builder()
      .id(USER_ID)
      .name(USERNAME)
      .role(Role.STUDENT)
      .batch(batch)
      .build();
    room = Room.builder()
      .assignment(assignment)
      .id(ROOM_ID)
      .student(student)
      .point(0)
      .build();
    comment = Comment.builder()
      .room(room)
      .author(student)
      .build();
    pageable = new PageRequest(0, 10);
    roomPage = PageHelper.toPage(Collections.singletonList(room), pageable);
    when(userService.getStudentsByBatchCode(BATCH_CODE)).thenReturn(
      Collections.singletonList(student));
    when(userService.getUser(USER_ID)).thenReturn(student);
    when(roomRepository.findByIdAndDeletedFalse(ROOM_ID)).thenReturn(
      Optional.of(room));
    when(roomRepository.findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID))
        .thenReturn(Optional.of(room));
    when(roomRepository.findAllByAssignmentIdAndDeletedFalse(
      ASSIGNMENT_ID)).thenReturn(Collections.singletonList(room));
    when(roomRepository.findAllByAssignmentIdAndDeletedFalse(ASSIGNMENT_ID,
                                                             pageable
    )).thenReturn(roomPage);
    when(roomRepository.save(any(Room.class))).thenReturn(room);
    when(roomRepository.findAllByStudentIdAndDeletedFalse(USER_ID, pageable)).thenReturn(
      new PageImpl<>(Collections.singletonList(room)));
    when(roomRepository.findAllByStudentIdAndDeletedFalse(USER_ID,
                                                          pageable
    )).thenReturn(roomPage);
    when(commentService.findAllCommentsByStudentIdAndAssignmentId(room, pageable)).thenReturn(
      PageHelper.toPage(Collections.singletonList(comment), pageable));
    when(commentService.createComment(comment)).thenReturn(comment);
    when(assignmentService.findById(ASSIGNMENT_ID, Role.STUDENT, BATCH_ID)).thenReturn(assignment);
    verify(assignmentService).addObserver(roomService);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(userService, roomRepository, commentService, assignmentService);
  }

  @Test
  public void findOrCreateRoomByStudentIdAndAssignmentIdExist() {

    Room actual = roomService.findOrCreateByStudentIdAndAssignmentId(USER_ID, USER_ID, ASSIGNMENT_ID);
    assertThat(actual).isEqualTo(room);
    verify(userService).getUser(USER_ID);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
  }

  @Test
  public void findOrCreateRoomByStudentIdAndAssignmentIdNotExist() {

    when(roomRepository.findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID))
        .thenReturn(Optional.empty());
    Room actual = roomService.findOrCreateByStudentIdAndAssignmentId(USER_ID, USER_ID, ASSIGNMENT_ID);
    assertThat(actual).isEqualTo(room);
    verify(userService, times(2)).getUser(USER_ID);
    verify(roomRepository).save(any(Room.class));
    verify(assignmentService).findById(ASSIGNMENT_ID, Role.STUDENT, BATCH_ID);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
  }

  @Test
  public void findAllByStudentId() {

    Page<Room> actual = roomService.findAllByStudentId(USER_ID, pageable);
    assertThat(actual.getContent().size()).isEqualTo(1);
    assertThat(actual.getContent().get(0)).isEqualTo(room);
    verify(roomRepository).findAllByStudentIdAndDeletedFalse(USER_ID, pageable);
  }

  @Test
  public void findAllByStudentIdAndPageable() {

    Page<Room> actual = roomService.findAllByStudentId(
      USER_ID, pageable, USER_ID);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent()
                 .get(0)).isEqualTo(room);
    verify(roomRepository).findAllByStudentIdAndDeletedFalse(USER_ID, pageable);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void findAllByStudentIdAndPageableAndUserIdNotEqualStudentIdWithRoleStudent() {

    String id = "id";
    when(userService.getUser(id)).thenReturn(User.builder()
                                               .id(id)
                                               .role(Role.STUDENT)
                                               .build());
    catchException(() -> roomService.findAllByStudentId(USER_ID, pageable, id));
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    verify(userService).getUser(id);
  }

  @Test
  public void findAllByStudentIdAndPageableAndUserIdNotEqualStudentIdWithRoleAdmin() {

    String id = "id";
    when(userService.getUser(id)).thenReturn(User.builder()
                                               .id(id)
                                               .role(Role.ADMIN)
                                               .build());
    Page<Room> actual = roomService.findAllByStudentId(USER_ID, pageable, id);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent()
                 .get(0)).isEqualTo(room);
    verify(roomRepository).findAllByStudentIdAndDeletedFalse(USER_ID, pageable);
    verify(userService).getUser(id);
  }

  @Test
  public void findAllByStudentIdAndPageableRepositoryReturnNull() {

    when(roomRepository.findAllByStudentIdAndDeletedFalse(USER_ID,
                                                          pageable
    )).thenReturn(null);
    Page<Room> actual = roomService.findAllByStudentId(
      USER_ID, pageable, USER_ID);
    assertThat(actual).isEqualTo(PageHelper.empty(pageable));
    verify(roomRepository).findAllByStudentIdAndDeletedFalse(USER_ID, pageable);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void findAllCommentsByRoomId() {

    Page<Comment> actual = roomService.findAllCommentsByStudentIdAndAssignmentId(
      USER_ID, ASSIGNMENT_ID, pageable);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getSize()).isEqualTo(10);
    assertThat(actual.getContent()
                 .get(0)).isEqualTo(comment);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
    verify(commentService).findAllCommentsByStudentIdAndAssignmentId(room, pageable);
  }

  @Test
  public void createComment() {

    Comment actual = roomService.createComment(comment, USER_ID);
    assertThat(actual.getRoom()).isEqualTo(room);
    assertThat(actual.getAuthor()).isEqualTo(student);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
    verify(userService, times(2)).getUser(USER_ID);
    verify(commentService).createComment(comment);
  }

  @Test
  public void createCommentDifferentStudent() {

    User anotherStudent = User.builder()
      .id("student-2")
      .role(Role.STUDENT)
      .build();
    comment.setAuthor(anotherStudent);
    when(userService.getUser("student-2")).thenReturn(anotherStudent);
    catchException(() -> roomService.createComment(comment, "student-2"));
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
    verify(userService).getUser("student-2");
  }

  @Test
  public void createCommentDeadlineHasPassed() {

    assignment.setDeadline(1500000);
    catchException(() -> roomService.createComment(comment, USER_ID));
    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void giveScoreToRoomByRoomId() {

    student.setBatch(batch);
    User mentor = User.builder()
      .id(MENTOR_ID)
      .role(Role.MENTOR)
      .batch(batch)
      .build();
    when(userService.getUser(MENTOR_ID)).thenReturn(mentor);
    room.setPoint(100);
    Room actual = roomService.giveScoreToRoomByStudentIdAndAssignmentId(USER_ID, MENTOR_ID, ASSIGNMENT_ID, 100);
    assertThat(actual.getId()).isEqualTo(ROOM_ID);
    assertThat(actual.getPoint()).isEqualTo(100);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
    verify(userService).getUser(MENTOR_ID);
    verify(roomRepository).save(room);
  }

  @Test
  public void giveScoreToRoomByRoomIdByStudent() {

    catchException(
      () -> roomService.giveScoreToRoomByStudentIdAndAssignmentId(USER_ID, USER_ID, ASSIGNMENT_ID,  100));
    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void deleteRoomByStudentIdAndAssignmentId() {

    roomService.deleteRoomByStudentIdAndAssignmentId(USER_ID, ASSIGNMENT_ID);
    verify(roomRepository).findByStudentIdAndAssignmentIdAndDeletedFalse(USER_ID, ASSIGNMENT_ID);
    room.setDeleted(true);
    verify(roomRepository).save(room);
    verify(commentService).deleteAllCommentByRoomId(ROOM_ID);
  }

  @Test
  public void deleteRoomById() {

    roomService.deleteRoomById(ROOM_ID);
    verify(roomRepository).findByIdAndDeletedFalse(ROOM_ID);
    room.setDeleted(true);
    verify(roomRepository).save(room);
    verify(commentService).deleteAllCommentByRoomId(ROOM_ID);
  }

  @Test
  public void deleteAllRoomsByAssignmentId() {

    roomService.deleteAllRoomsByAssignmentId(ASSIGNMENT_ID);
    verify(roomRepository).findAllByAssignmentIdAndDeletedFalse(ASSIGNMENT_ID);
    room.setDeleted(true);
    verify(roomRepository).save(room);
    verify(commentService).deleteAllCommentByRoomId(ROOM_ID);
  }

  @Test
  public void updateObserverTest() {
    roomService.update(new Observable(), assignment);
    verify(roomRepository).findAllByAssignmentIdAndDeletedFalse(ASSIGNMENT_ID);
    room.setDeleted(true);
    verify(roomRepository).save(room);
    verify(commentService).deleteAllCommentByRoomId(ROOM_ID);
  }

  @Test
  public void updateObserverAnyArgumentTest() {
    roomService.update(new Observable(), new Object());
  }
}
