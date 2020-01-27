package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {

  Room findOrCreateByStudentIdAndAssignmentId(String id, String userId, String assignmentId);

  Page<Comment> findAllCommentsByStudentIdAndAssignmentId(String studentId, String assignmentId, Pageable pageable);

  Page<Room> findAllByStudentId(
    String studentId, Pageable pageable, String userId
  );

  Page<Room> findAllByStudentId(String studentId, Pageable pageable);

  Comment createComment(Comment comment, String userId);

  Room giveScoreToRoomByStudentIdAndAssignmentId(String studentId, String userId, String assignmentId, Integer point);

  void deleteRoomByStudentIdAndAssignmentId(String studentId, String assignmentId);

  void deleteRoomById(String roomId);

  void deleteAllRoomsByAssignmentId(String assignmentId);

}
