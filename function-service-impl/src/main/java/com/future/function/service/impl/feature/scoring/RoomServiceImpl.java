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
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.PageHelper;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private UserService userService;
    private CommentService commentService;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, UserService userService, CommentService commentService) {
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Override
    public Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable) {
        return Optional.ofNullable(assignmentId)
                .map(id -> roomRepository.findAllByAssignmentIdAndDeletedFalse(assignmentId, pageable))
                .orElseGet(() -> PageHelper.empty(pageable));
    }

    @Override
    public Room findById(String id, String userId) {
      User user = userService.getUser(userId);
        return Optional.ofNullable(id)
                .flatMap(roomRepository::findByIdAndDeletedFalse)
                .filter(room -> AuthorizationHelper.isUserAuthorizedForAccess(user, room.getStudent().getId(),
                    AuthorizationHelper.getScoringAllowedRoles()))
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }

    @Override
    public Page<Comment> findAllCommentsByRoomId(String roomId, Pageable pageable) {
        return commentService.findAllCommentsByRoomId(roomId, pageable);
    }

    @Override
    public Page<Room> findAllByStudentId(String studentId, Pageable pageable, String userId) {
        return Optional.ofNullable(userId)
                .map(userService::getUser)
                .filter(user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentId,
                    AuthorizationHelper.getScoringAllowedRoles()))
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
                .map(roomId -> this.findById(roomId, userId))
                .filter(this::validateAssignmentDeadlineNotPassed)
                .map(currentRoom -> setRoomAndAuthor(comment, userId, currentRoom))
                .map(commentService::createComment)
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #createComment #RoomService"));
    }

  private Comment setRoomAndAuthor(Comment comment, String userId, Room currentRoom) {
    comment.setRoom(currentRoom);
    User author = userService.getUser(userId);
    comment.setAuthor(author);
    return comment;
  }

  private boolean validateAssignmentDeadlineNotPassed(Room room) {
    return room.getAssignment().getDeadline() > new Date().getTime();
  }

  @Override
    public Assignment createRoomsByAssignment(Assignment assignment) {
        return Optional.ofNullable(assignment)
            .map(Assignment::getBatch)
            .map(Batch::getCode)
            .map(userService::getStudentsByBatchCode)
            .map(list -> mapEveryStudentToRoom(assignment, list))
            .map(object -> assignment)
                .orElseThrow(() -> new UnsupportedOperationException("Failed on #createRoomsByAssignment #RoomService"));
    }

  private List<User> mapEveryStudentToRoom(Assignment assignment, List<User> list) {
    list.forEach(user -> this.createRoomForUserAndSave(user, assignment));
    return list;
  }

  private void createRoomForUserAndSave(User user, Assignment assignment) {
        Room room = Room
                .builder()
                .assignment(assignment)
                .point(0)
                .student(user)
                .build();
        roomRepository.save(room);
    }

    @Override
    public Room giveScoreToRoomByRoomId(String roomId, String userId, Integer point) {
      User user = userService.getUser(userId);
      return Optional.ofNullable(roomId)
          .map(id -> this.findById(id, user.getId()))
          .filter(room -> AuthorizationHelper.isAuthorizedForEdit(user.getEmail(), user.getRole(), room, Role.MENTOR))
          .map(room -> setRoomPoint(point, room))
          .map(roomRepository::save)
          .orElseThrow(() -> new ForbiddenException("Failed at #giveScoreToRoomByRoomId #RoomService"));
    }

  private Room setRoomPoint(Integer point, Room room) {
    room.setPoint(point);
    return room;
  }

  @Override
    public void deleteRoomById(String id) {
        Optional.ofNullable(id)
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
    list.forEach(this::setDeleteAsTrueAndSave);
  }

  private void setDeleteAsTrueAndSave(Room room) {
        commentService.deleteAllCommentByRoomId(room.getId());
        room.setDeleted(true);
        roomRepository.save(room);
    }
}
