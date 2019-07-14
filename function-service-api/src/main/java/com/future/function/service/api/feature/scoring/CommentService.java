package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;

import java.util.List;

public interface CommentService {

  List<Comment> findAllCommentsByRoomId(String roomId);

  Comment createCommentByRoom(Room room, Comment comment);

  void deleteAllCommentByRoomId(String roomId);
}
