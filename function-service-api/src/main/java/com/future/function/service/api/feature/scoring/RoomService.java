package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {

  Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable);

  Room findById(String id, String userId);

  Page<Comment> findAllCommentsByRoomId(String roomId, Pageable pageable);

    Page<Room> findAllByStudentId(String studentId, Pageable pageable, String userId);

    List<Room> findAllByStudentId(String studentId);

  Comment createComment(Comment comment, String userId);

  Assignment createRoomsByAssignment(Assignment assignment);

  Room giveScoreToRoomByRoomId(String roomId, String userId, Integer point);

  void deleteRoomById(String id);

  void deleteAllRoomsByAssignmentId(String assignmentId);

}
