package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import com.future.function.web.model.response.feature.scoring.CommentWebResponse;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;


public class CommentResponseMapperTest {

  private static final String ROOM_ID = "room-id";
  private static final String USER_ID = "user-id";
  private static final String COMMENT = "text";

  private Room room;
  private User author;
  private Comment comment;
  private AuthorWebResponse authorWebResponse;
  private CommentWebResponse commentWebResponse;

  @Before
  public void setUp() throws Exception {
    room = Room.builder().id(ROOM_ID).build();
    author = User.builder().id(USER_ID).name("name").build();
    authorWebResponse = AuthorWebResponse.builder().id(USER_ID).name("name").build();
    comment = Comment.builder().id("id").room(room).author(author).text(COMMENT).build();
    commentWebResponse = CommentWebResponse.builder().id("id").comment(COMMENT).author(authorWebResponse).build();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void toListCommentWebResponse() {
    DataResponse<List<CommentWebResponse>> actual = CommentResponseMapper
        .toDataListCommentWebResponse(Collections.singletonList(comment));
    assertThat(actual.getData()).isEqualTo(Collections.singletonList(commentWebResponse));
  }

  @Test
  public void toDataCommentWebResponse() {
    DataResponse<CommentWebResponse> actual = CommentResponseMapper.toDataCommentWebResponse(HttpStatus.OK, comment);
    assertThat(actual.getData()).isEqualTo(commentWebResponse);
    assertThat(actual.getCode()).isEqualTo(200);
  }
}