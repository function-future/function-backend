package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import com.future.function.web.model.response.feature.scoring.CommentWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentResponseMapper {

  public static PagingResponse<CommentWebResponse> toPagingCommentWebResponse(Page<Comment> commentPage) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, CommentResponseMapper.toListCommentWebResponse(commentPage.getContent()),
        PageHelper.toPaging(commentPage));
  }

  private static List<CommentWebResponse> toListCommentWebResponse(List<Comment> commentList) {
    return commentList
        .stream()
        .map(CommentResponseMapper::buildCommentWebResponse)
        .collect(Collectors.toList());
  }

  public static DataResponse<CommentWebResponse> toDataCommentWebResponse(HttpStatus httpStatus, Comment comment) {
    return ResponseHelper.toDataResponse(httpStatus, buildCommentWebResponse(comment));
  }

  private static CommentWebResponse buildCommentWebResponse(Comment comment) {
    return CommentWebResponse
        .builder()
        .comment(comment.getText())
        .createdAt(comment.getCreatedAt())
        .author(buildAuthorWebResponse(comment))
        .id(comment.getId())
        .build();
  }

  private static AuthorWebResponse buildAuthorWebResponse(Comment comment) {
    return AuthorWebResponse.builder().id(comment.getAuthor().getId()).name(comment.getAuthor().getName()).build();
  }

}
