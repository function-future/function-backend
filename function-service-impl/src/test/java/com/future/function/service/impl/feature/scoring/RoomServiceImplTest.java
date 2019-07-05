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
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.impl.helper.PageHelper;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.data.domain.PageRequest;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceImplTest {

  private static final String ROOM_ID = "room-id";
  private static final String ASSIGNMENT_ID = "assignment-id";
  private static final long DEADLINE = 15000000;
  private static final String USER_ID = "userId";
  private static final String USERNAME = "userName";
  private static final String BATCH_CODE = "batchCode";
  private static final String MENTOR_ID = "mentor-id";

  private Room room;
  private Assignment assignment;
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

  @Before
  public void setUp() throws Exception {
    batch = Batch.builder().code(BATCH_CODE).build();
    assignment = Assignment.builder().id(ASSIGNMENT_ID).batch(batch)
        .deadline(new Date(2019, 11, 12).getTime()).build();
    student = User.builder().id(USER_ID).name(USERNAME).role(Role.STUDENT).build();
    room = Room.builder().assignment(assignment).id(ROOM_ID).student(student).point(0).build();
    comment = Comment.builder().room(room).author(student).build();
    when(userService.getStudentsByBatchCode(BATCH_CODE)).thenReturn(Collections.singletonList(student));
    when(userService.getUser(USER_ID)).thenReturn(student);
    when(roomRepository.findByIdAndDeletedFalse(ROOM_ID)).thenReturn(Optional.of(room));
    when(roomRepository.findAllByAssignmentIdAndDeletedFalse(ASSIGNMENT_ID)).thenReturn(Collections.singletonList(room));
    when(roomRepository.findAllByAssignmentIdAndDeletedFalse(ASSIGNMENT_ID, new PageRequest(0, 10)))
        .thenReturn(PageHelper.toPage(Collections.singletonList(room), new PageRequest(0, 10)));
    when(roomRepository.save(any(Room.class))).thenReturn(room);
    when(commentService.findAllCommentsByRoomId(ROOM_ID)).thenReturn(Collections.singletonList(comment));
    when(commentService.createCommentByRoom(room, comment)).thenReturn(comment);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(userService, roomRepository, commentService);
  }

  @Test
  public void findAllRoomsByAssignmentId() {
    Page<Room> actual = roomService.findAllRoomsByAssignmentId(ASSIGNMENT_ID, new PageRequest(0, 10));
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent().get(0)).isEqualTo(room);
    verify(roomRepository).findAllByAssignmentIdAndDeletedFalse(ASSIGNMENT_ID, new PageRequest(0, 10));
  }

  @Test
  public void findById() {
    Room actual = roomService.findById(ROOM_ID, USER_ID);
    assertThat(actual).isEqualTo(room);
    verify(userService).getUser(USER_ID);
    verify(roomRepository).findByIdAndDeletedFalse(ROOM_ID);
  }

  @Test
  public void findAllCommentsByRoomId() {
    List<Comment> actual = roomService.findAllCommentsByRoomId(ROOM_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)).isEqualTo(comment);
    verify(commentService).findAllCommentsByRoomId(ROOM_ID);
  }

  @Test
  public void createComment() {
    Comment actual = roomService.createComment(comment, USER_ID);
    assertThat(actual.getRoom()).isEqualTo(room);
    assertThat(actual.getAuthor()).isEqualTo(student);
    verify(roomRepository).findByIdAndDeletedFalse(ROOM_ID);
    verify(userService, times(2)).getUser(USER_ID);
    verify(commentService).createCommentByRoom(room, comment);
  }

  @Test
  public void createCommentDifferentStudent() {
    User anotherStudent = User.builder().id("student-2").role(Role.STUDENT).build();
    comment.setAuthor(anotherStudent);
    when(userService.getUser("student-2")).thenReturn(anotherStudent);
    catchException(() -> roomService.createComment(comment, "student-2"));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(roomRepository).findByIdAndDeletedFalse(ROOM_ID);
    verify(userService).getUser("student-2");
  }

  @Test
  public void createCommentDeadlineHasPassed() {
    assignment.setDeadline(1500000);
    catchException(() -> roomService.createComment(comment, USER_ID));
    assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
    verify(roomRepository).findByIdAndDeletedFalse(ROOM_ID);
    verify(userService, times(2)).getUser(USER_ID);
  }

  @Test
  public void createRoomsByAssignment() {
    Assignment actual = roomService.createRoomsByAssignment(assignment);
    assertThat(actual).isEqualTo(assignment);
    verify(userService).getStudentsByBatchCode(BATCH_CODE);
    verify(roomRepository).save(any(Room.class));
  }

  @Test
  public void giveScoreToRoomByRoomId() {
    student.setBatch(batch);
    User mentor = User.builder().id(MENTOR_ID).role(Role.MENTOR).batch(batch).build();
    when(userService.getUser(MENTOR_ID)).thenReturn(mentor);
    room.setPoint(100);
    Room actual = roomService.giveScoreToRoomByRoomId(ROOM_ID, MENTOR_ID, 100);
    assertThat(actual.getId()).isEqualTo(ROOM_ID);
    assertThat(actual.getPoint()).isEqualTo(100);
    verify(roomRepository).findByIdAndDeletedFalse(ROOM_ID);
    verify(userService, times(2)).getUser(MENTOR_ID);
    verify(roomRepository).save(room);
  }

  @Test
  public void giveScoreToRoomByRoomIdByStudent() {
    room.setPoint(100);
    catchException(() -> roomService.giveScoreToRoomByRoomId(ROOM_ID, USER_ID, 100));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(userService).getUser(USER_ID);
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
}
