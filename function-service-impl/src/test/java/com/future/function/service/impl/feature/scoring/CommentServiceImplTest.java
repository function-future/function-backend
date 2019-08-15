package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.CommentRepository;
import com.future.function.service.impl.helper.PageHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

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

  private Pageable pageable;

  @InjectMocks
  private CommentServiceImpl commentService;

  @Mock
  private CommentRepository commentRepository;

  @Before
  public void setUp() throws Exception {

    author = User.builder()
      .id(USER_ID)
      .name(USERNAME)
      .build();
    room = Room.builder()
      .student(author)
      .id(ROOM_ID)
      .build();
    comment = Comment.builder()
      .author(author)
      .room(room)
      .build();

    pageable = new PageRequest(0, 10);

    when(commentRepository.findAllByRoomIdOrderByCreatedAtDesc(
      ROOM_ID)).thenReturn(Collections.singletonList(comment));
    when(commentRepository.findAllByRoomIdOrderByCreatedAtDesc(ROOM_ID,
                                                               pageable
    )).thenReturn(
      PageHelper.toPage(Collections.singletonList(comment), pageable));
    when(commentRepository.save(comment)).thenReturn(comment);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(commentRepository);
  }

  @Test
  public void findAllCommentsByRoomId() {

    Page<Comment> actual = commentService.findAllCommentsByRoomId(
      ROOM_ID, pageable);
    assertThat(actual.getSize()).isEqualTo(10);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent()
                 .get(0)).isEqualTo(comment);
    verify(commentRepository).findAllByRoomIdOrderByCreatedAtDesc(
      ROOM_ID, pageable);
  }

  @Test
  public void createCommentByRoom() {

    Comment actual = commentService.createComment(comment);
    assertThat(actual.getRoom()).isEqualTo(room);
    assertThat(actual.getAuthor()).isEqualTo(author);
    verify(commentRepository).save(comment);
  }

  @Test
  public void deleteAllCommentByRoomId() {

    commentService.deleteAllCommentByRoomId(ROOM_ID);
    verify(commentRepository).findAllByRoomIdOrderByCreatedAtDesc(ROOM_ID);
    comment.setDeleted(true);
    verify(commentRepository).save(comment);
  }

}
