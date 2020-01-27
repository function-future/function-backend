package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

  Page<Comment> findAllCommentsByStudentIdAndAssignmentId(Room room, Pageable pageable);

  Comment createComment(Comment comment);

  void deleteAllCommentByRoomId(String roomId);

}
