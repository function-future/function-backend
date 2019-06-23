package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {

    Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable);

    List<Room> findAllRoomsByStudentId(String studentId);

    Room findById(String id);

    List<Comment> findAllCommentsByRoomId(String roomId);

    Comment createComment(Comment comment);

    Assignment createRoomsByAssignment(Assignment assignment);

    Room giveScoreToRoomByRoomId(String roomId, Integer point);

    void deleteRoomById(String id);

    void deleteAllRoomsByAssignmentId(String assignmentId);

}
