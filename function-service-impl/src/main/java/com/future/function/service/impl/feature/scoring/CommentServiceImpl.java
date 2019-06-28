package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.CommentRepository;
import com.future.function.service.api.feature.scoring.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAllCommentsByRoomId(String roomId) {
        return Optional.ofNullable(roomId)
                .map(commentRepository::findAllByRoomId)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Comment createCommentByRoom(Room room, Comment comment) {
        comment.setRoom(room);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteAllCommentByRoomId(String roomId) {
        this.findAllCommentsByRoomId(roomId)
                .stream()
                .forEach(comment -> {
                    comment.setDeleted(true);
                    commentRepository.save(comment);
                });
    }
}
