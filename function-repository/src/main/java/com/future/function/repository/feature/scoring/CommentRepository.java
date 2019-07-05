package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

  List<Comment> findAllByRoomId(String roomId);

}
