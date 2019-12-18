package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
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

  private static final String COMMENT = "text";

  private static final String USER_ID = "user-id";

  private static final String ASSIGNMENT_ID = "assignment-id";

  private CommentWebRequest commentWebRequest;

  private Comment comment;

  private Room room;

  @InjectMocks
  private CommentRequestMapper commentRequestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() throws Exception {

    commentWebRequest = CommentWebRequest.builder()
      .comment(COMMENT)
      .build();
    room = Room.builder()
      .student(User.builder().id(USER_ID).build())
      .assignment(Assignment.builder().id(ASSIGNMENT_ID).build())
      .build();
    comment = Comment.builder()
      .room(room)
      .build();

    when(validator.validate(commentWebRequest)).thenReturn(commentWebRequest);

  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(validator);
  }

  @Test
  public void toCommentFromRequestWithRoomId() {

    Comment actual = commentRequestMapper.toCommentFromRequestWithStudentIdAndAssignmentId(
      commentWebRequest, USER_ID, ASSIGNMENT_ID);
    assertThat(actual.getRoom().getStudent().getId()).isEqualTo(room.getStudent().getId());
    assertThat(actual.getRoom().getAssignment().getId()).isEqualTo(room.getAssignment().getId());
    verify(validator).validate(commentWebRequest);
  }

}
