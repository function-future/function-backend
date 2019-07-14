package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

  List<Comment> findAllByRoomId(String roomId);

}
