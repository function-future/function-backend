package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.CommentRequestMapper;
import com.future.function.web.mapper.response.scoring.CommentResponseMapper;
import com.future.function.web.model.request.scoring.CommentWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.CommentWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest extends TestHelper {

  private static final String BATCH_CODE = "3";
  private static final String ROOM_ID = "room-id";
  private static final String USER_ID = STUDENT_SESSION_ID;
  private static final String USER_NAME = "user-name";
  private static final String COMMENT_ID = "text-id";
  private static final String COMMENT = "text";
  private static final String ASSIGNMENT_ID = "assignment-id";


  private Comment comment;
  private Room room;
  private User user;
  private List<Comment> commentList;
  private CommentWebRequest commentWebRequest;

  private DataResponse<CommentWebResponse> DATA_RESPONSE;

  private BaseResponse BASE_RESPONSE;

  private DataResponse<CommentWebResponse> CREATED_DATA_RESPONSE;

  private DataResponse<List<CommentWebResponse>> LIST_DATA_RESPONSE;

  private JacksonTester<DataResponse<List<CommentWebResponse>>> pagingResponseJacksonTester;

  private JacksonTester<CommentWebRequest> roomPointWebRequestJacksonTester;

  @MockBean
  private RoomService roomService;

  @MockBean
  private CommentRequestMapper commentRequestMapper;

  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.STUDENT);
    user = User.builder().id(USER_ID)
        .name(USER_NAME)
        .address("address")
        .phone("phone")
        .email("email")
        .batch(Batch.builder().code(BATCH_CODE).build())
        .role(Role.STUDENT)
        .pictureV2(null)
        .build();

    room = Room.builder()
        .student(user)
        .point(0)
        .build();

    comment = Comment.builder().author(user).text(COMMENT).id(COMMENT_ID).room(room).build();

    commentWebRequest = CommentWebRequest.builder().userId(USER_ID).comment(COMMENT).build();

    DATA_RESPONSE = CommentResponseMapper
        .toDataCommentWebResponse(HttpStatus.OK, this.comment);

    CREATED_DATA_RESPONSE = CommentResponseMapper
        .toDataCommentWebResponse(HttpStatus.CREATED, this.comment);

    LIST_DATA_RESPONSE = CommentResponseMapper
        .toDataListCommentWebResponse(Collections.singletonList(this.comment));

    BASE_RESPONSE = ResponseHelper.toBaseResponse(HttpStatus.OK);

    when(roomService.findAllCommentsByRoomId(ROOM_ID))
        .thenReturn(Collections.singletonList(comment));
    when(roomService.createComment(comment, STUDENT_ID))
        .thenReturn(comment);
    when(commentRequestMapper.toCommentFromRequestWithRoomId(commentWebRequest, ROOM_ID))
        .thenReturn(comment);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(roomService, commentRequestMapper);
  }

  @Test
  public void findAllCommentsByRoomId() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms/" + ROOM_ID +
            "/comments").cookie(cookies))
        .andExpect(status().isOk())
        .andExpect(content().json(
            pagingResponseJacksonTester.write(LIST_DATA_RESPONSE).getJson()));
    verify(roomService).findAllCommentsByRoomId(ROOM_ID);
  }

  @Test
  public void createComment() throws Exception {
    mockMvc.perform(
        post("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms/" + ROOM_ID +
            "/comments")
            .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(roomPointWebRequestJacksonTester.write(commentWebRequest).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().json(
            dataResponseJacksonTester.write(CREATED_DATA_RESPONSE).getJson()));
    verify(roomService).createComment(comment, STUDENT_ID);
    verify(commentRequestMapper).toCommentFromRequestWithRoomId(commentWebRequest, ROOM_ID);
  }
}
