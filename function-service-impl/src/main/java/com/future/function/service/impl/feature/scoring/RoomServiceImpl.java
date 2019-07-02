package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.RoomRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            .map(room -> checkStudentEligibility(user, room))
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }

  private Room checkStudentEligibility(User user, Room room) {
    if (!user.getRole().equals(Role.STUDENT)) {
      return room;
    } else if (room.getStudent().getId().equals(user.getId())) {
      return room;
    }
    throw new ForbiddenException("User not allowed");
  }

    @Override
    public List<Comment> findAllCommentsByRoomId(String roomId) {
        return commentService.findAllCommentsByRoomId(roomId);
    }

    @Override
    public Page<Room> findAllByStudentId(String studentId, Pageable pageable) {
        return Optional.ofNullable(studentId)
                .map(userService::getUser)
                .map(User::getId)
                .map(id -> roomRepository.findAllByStudentIdAndDeletedFalse(id, pageable))
                .orElseGet(() -> PageHelper.empty(pageable));
    }

    @Override
    public Comment createComment(Comment comment, String userId) {
      Room room = this.findById(comment.getRoom().getId(), userId);
      User author = userService.getUser(userId);
        return Optional.of(comment)
                .filter(value -> room.getAssignment().getDeadline() > new Date().getTime())
                .map(value -> {
                  comment.setRoom(room);
                  comment.setAuthor(author);
                  return comment;
                })
                .map(value -> commentService.createCommentByRoom(room, value))
                .orElseThrow(() -> new UnsupportedOperationException("Deadline has passed"));
    }

    @Override
    public Assignment createRoomsByAssignment(Assignment assignment) {
        List<User> userListFromBatch = userService.getStudentsByBatchCode(assignment.getBatch().getCode());
        userListFromBatch
                .forEach(user -> this.createRoomForUserAndSave(user, assignment));
        return assignment;
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
          .filter(condition -> user.getRole().equals(Role.MENTOR))
          .map(id -> this.findById(id, user.getId()))
          .filter(room -> validateMentorBatch(user, room))
          .map(room -> {
            room.setPoint(point);
            return room;
          })
          .map(roomRepository::save)
          .orElseThrow(() -> new ForbiddenException("User not allowed"));
    }

  private boolean validateMentorBatch(User user, Room room) {
    return room.getStudent().getBatch().getCode().equals(user.getBatch().getCode());
    }

    @Override
    public void deleteRoomById(String id) {
        Optional.ofNullable(id)
                .flatMap(roomRepository::findByIdAndDeletedFalse)
                .map(room -> {
                    commentService.deleteAllCommentByRoomId(room.getId());
                    return room;
                })
                .ifPresent(this::setDeleteAsTrueAndSave);
    }

    @Override
    public void deleteAllRoomsByAssignmentId(String assignmentId) {
        Optional.ofNullable(assignmentId)
                .map(roomRepository::findAllByAssignmentIdAndDeletedFalse)
                .ifPresent(list -> list.forEach(room -> {
                    commentService.deleteAllCommentByRoomId(room.getId());
                    this.setDeleteAsTrueAndSave(room);
                }));
    }

    private void setDeleteAsTrueAndSave(Room room) {
        room.setDeleted(true);
        roomRepository.save(room);
    }
}
