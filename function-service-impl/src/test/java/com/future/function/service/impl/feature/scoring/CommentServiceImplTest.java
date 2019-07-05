package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.CommentRepository;
import java.util.Collections;
import java.util.List;
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
public class CommentServiceImplTest {

  private static final String ROOM_ID = "room-id";
  private static final String USER_ID = "user-id";
  private static final String USERNAME = "userName";
  private static final String COMMENT = "text";

  private Room room;
  private User author;
  private Comment comment;

  @InjectMocks
  private CommentServiceImpl commentService;

  @Mock
  private CommentRepository commentRepository;

  @Before
  public void setUp() throws Exception {
    author = User.builder().id(USER_ID).name(USERNAME).build();
    room = Room.builder().student(author).id(ROOM_ID).build();
    comment = Comment.builder().author(author).room(room).build();

    when(commentRepository.findAllByRoomId(ROOM_ID)).thenReturn(Collections.singletonList(comment));
    when(commentRepository.save(comment)).thenReturn(comment);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(commentRepository);
  }

  @Test
  public void findAllCommentsByRoomId() {
    List<Comment> actual = commentService.findAllCommentsByRoomId(ROOM_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)).isEqualTo(comment);
    verify(commentRepository).findAllByRoomId(ROOM_ID);
  }

  @Test
  public void createCommentByRoom() {
    Comment actual = commentService.createCommentByRoom(room, comment);
    assertThat(actual.getRoom()).isEqualTo(room);
    assertThat(actual.getAuthor()).isEqualTo(author);
    verify(commentRepository).save(comment);
  }

  @Test
  public void deleteAllCommentByRoomId() {
    commentService.deleteAllCommentByRoomId(ROOM_ID);
    verify(commentRepository).findAllByRoomId(ROOM_ID);
    comment.setDeleted(true);
    verify(commentRepository).save(comment);
  }
}