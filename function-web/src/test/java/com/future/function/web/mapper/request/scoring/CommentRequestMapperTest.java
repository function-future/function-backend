package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.CommentWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentRequestMapperTest {

  private static final String ROOM_ID = "room-id";
  private static final String USER_ID = "user-id";
  private static final String COMMENT = "text";

  private CommentWebRequest commentWebRequest;
  private Comment comment;
  private Room room;
  private User user;

  @InjectMocks
  private CommentRequestMapper commentRequestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() throws Exception {

    commentWebRequest = CommentWebRequest.builder().userId(USER_ID).comment(COMMENT).build();
    room = Room.builder().id(ROOM_ID).build();
    user = User.builder().id(USER_ID).build();
    comment = Comment.builder().room(room).author(user).build();

    when(validator.validate(commentWebRequest)).thenReturn(commentWebRequest);

  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(validator);
  }

  @Test
  public void toCommentFromRequestWithRoomId() {
    Comment actual = commentRequestMapper.toCommentFromRequestWithRoomId(commentWebRequest, ROOM_ID);
    assertThat(actual.getRoom()).isEqualTo(room);
    assertThat(actual.getAuthor()).isEqualTo(user);
    verify(validator).validate(commentWebRequest);
  }
}