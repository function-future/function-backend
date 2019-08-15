package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

  Page<Comment> findAllCommentsByRoomId(String roomId, Pageable pageable);

  Comment createComment(Comment comment);

  void deleteAllCommentByRoomId(String roomId);

}
