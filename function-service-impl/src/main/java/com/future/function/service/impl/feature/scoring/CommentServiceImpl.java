package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.CommentRepository;
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

  private CommentRepository commentRepository;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository) {

    this.commentRepository = commentRepository;
  }

  @Override
  public Page<Comment> findAllCommentsByStudentIdAndAssignmentId(
    Room room, Pageable pageable
  ) {

    return Optional.ofNullable(room)
      .map(Room::getId)
      .map(id -> commentRepository.findAllByRoomIdOrderByCreatedAtDesc(id, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public Comment createComment(Comment comment) {

    return commentRepository.save(comment);
  }

  @Override
  public void deleteAllCommentByRoomId(String roomId) {

    commentRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId)
      .forEach(this::setDeletedAndSaveComment);
  }

  private void setDeletedAndSaveComment(Comment comment) {

    comment.setDeleted(true);
    commentRepository.save(comment);
  }

}
