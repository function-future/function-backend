package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Page<Comment> findAllByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);

    List<Comment> findAllByRoomIdOrderByCreatedAtDesc(String roomId);

}
