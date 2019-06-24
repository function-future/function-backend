package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {

  Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable);

  Room findById(String id);

  List<Comment> findAllCommentsByRoomId(String roomId);

  Comment createComment(Comment comment);

  Assignment createRoomsByAssignment(Assignment assignment);

  Room giveScoreToRoomByRoomId(String roomId, Integer point);

  void deleteRoomById(String id);

  void deleteAllRoomsByAssignmentId(String assignmentId);

}
