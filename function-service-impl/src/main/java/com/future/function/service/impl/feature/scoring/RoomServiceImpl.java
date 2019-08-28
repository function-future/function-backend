package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.RoomRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.PageHelper;
import java.util.Observable;
import java.util.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService, Observer {

  private RoomRepository roomRepository;

  private UserService userService;

  private CommentService commentService;

  private AssignmentService assignmentService;

  @Autowired
  public RoomServiceImpl(RoomRepository roomRepository, UserService userService, CommentService commentService,
      AssignmentService assignmentService) {
    this.roomRepository = roomRepository;
    this.userService = userService;
    this.commentService = commentService;
    this.assignmentService = assignmentService;

    this.assignmentService.addObserver(this);
  }

  @Override
  public Room findOrCreateByStudentIdAndAssignmentId(String studentId, String userId, String assignmentId) {

    return Optional.ofNullable(userId)
        .map(userService::getUser)
        .filter(user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentId,
            AuthorizationHelper.getScoringAllowedRoles()))
        .map(ignored -> studentId)
      .flatMap(id -> roomRepository.findByStudentIdAndAssignmentIdAndDeletedFalse(id, assignmentId))
      .orElseGet(() -> createNewRoomForStudent(studentId, assignmentId));
  }

  private Room createNewRoomForStudent(String studentId, String assignmentId) {
    User student = userService.getUser(studentId);
    Assignment assignment = assignmentService.findById(assignmentId);
    return this.createRoomForUserAndSave(student, assignment);
  }

  @Override
  public Page<Comment> findAllCommentsByRoomId(
    String roomId, Pageable pageable
  ) {

    return commentService.findAllCommentsByRoomId(roomId, pageable);
  }

  @Override
  public Page<Room> findAllByStudentId(
    String studentId, Pageable pageable, String userId
  ) {

    return Optional.ofNullable(userId)
      .map(userService::getUser)
      .filter(
        user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentId,
                                                              AuthorizationHelper.getScoringAllowedRoles()
        ))
      .map(ignored -> studentId)
      .map(id -> roomRepository.findAllByStudentIdAndDeletedFalse(id, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public List<Room> findAllByStudentId(String studentId) {

    return Optional.ofNullable(studentId)
      .map(roomRepository::findAllByStudentIdAndDeletedFalse)
      .orElseGet(ArrayList::new);
  }

  @Override
  public Comment createComment(Comment comment, String userId) {

    return Optional.of(comment)
      .map(Comment::getRoom)
      .map(Room::getId)
      .flatMap(roomRepository::findByIdAndDeletedFalse)
      .filter(room -> AuthorizationHelper.isUserAuthorizedForAccess(userService.getUser(userId),
          room.getStudent().getId(), AuthorizationHelper.getScoringAllowedRoles()))
      .filter(this::validateAssignmentDeadlineNotPassed)
      .map(currentRoom -> setRoomAndAuthor(comment, userId, currentRoom))
      .map(commentService::createComment)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #createComment #RoomService"));
  }

  private Comment setRoomAndAuthor(
    Comment comment, String userId, Room currentRoom
  ) {

    comment.setRoom(currentRoom);
    User author = userService.getUser(userId);
    comment.setAuthor(author);
    return comment;
  }

  private boolean validateAssignmentDeadlineNotPassed(Room room) {

    return room.getAssignment()
             .getDeadline() > new Date().getTime();
  }

  private Room createRoomForUserAndSave(User user, Assignment assignment) {

    Room room = Room.builder()
      .assignment(assignment)
      .point(0)
      .student(user)
      .build();
    return roomRepository.save(room);
  }

  @Override
  public Room giveScoreToRoomByStudentIdAndAssignmentId(
    String studentId, String userId, String assignmentId, Integer point
  ) {

    return Optional.ofNullable(userId)
      .map(userService::getUser)
      .filter(user -> user.getRole().equals(Role.MENTOR))
      .map(ignored -> studentId)
      .flatMap(id -> roomRepository.findByStudentIdAndAssignmentIdAndDeletedFalse(id, assignmentId))
      .map(room -> setRoomPoint(point, room))
      .map(roomRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #giveScoreToRoomByStudentIdAndAssignmentId #RoomService"));
  }

  private Room setRoomPoint(Integer point, Room room) {

    room.setPoint(point);
    return room;
  }

  @Override
  public void deleteRoomByStudentIdAndAssignmentId(String studentId, String assignmentId) {

    Optional.ofNullable(studentId)
      .flatMap(id -> roomRepository.findByStudentIdAndAssignmentIdAndDeletedFalse(studentId, assignmentId))
      .ifPresent(this::setDeleteAsTrueAndSave);
  }

  @Override
  public void deleteRoomById(String roomId) {
    Optional.ofNullable(roomId)
        .flatMap(roomRepository::findByIdAndDeletedFalse)
        .ifPresent(this::setDeleteAsTrueAndSave);
  }

  @Override
  public void deleteAllRoomsByAssignmentId(String assignmentId) {

    Optional.ofNullable(assignmentId)
      .map(roomRepository::findAllByAssignmentIdAndDeletedFalse)
      .ifPresent(this::setEveryRoomAsDeleted);
  }

  private void setEveryRoomAsDeleted(List<Room> list) {

    list.parallelStream().forEach(this::setDeleteAsTrueAndSave);
  }

  private void setDeleteAsTrueAndSave(Room room) {

    commentService.deleteAllCommentByRoomId(room.getId());
    room.setDeleted(true);
    roomRepository.save(room);
  }

  @Override
  public void update(Observable o, Object arg) {
    if(arg instanceof Assignment) {
      this.deleteAllRoomsByAssignmentId(((Assignment) arg).getId());
    }
  }
}
