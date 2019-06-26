package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.scoring.RoomResponseMapper;
import com.future.function.web.model.request.scoring.RoomPointWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(RoomController.class)
public class RoomControllerTest extends TestHelper {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String BATCH_CODE = "3";
  private static String ASSIGNMENT_ID = UUID.randomUUID().toString();
  private static final String ROOM_ID = "room-id";
  private static final String USER_ID = "user-id";
  private static final String USER_NAME = "user-name";

  private Pageable pageable;
  private Assignment assignment;
  private Room room;
  private User user;
  private Page<Room> roomPage;
  private RoomPointWebRequest roomPointWebRequest;

  private DataResponse<RoomWebResponse> DATA_RESPONSE;

  private PagingResponse<RoomWebResponse> PAGING_RESPONSE;

  private BaseResponse BASE_RESPONSE;

  private JacksonTester<RoomPointWebRequest> roomPointWebRequestJacksonTester;

  @MockBean
  private AssignmentService assignmentService;

  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.ADMIN);
    assignment = Assignment
        .builder()
        .id(ASSIGNMENT_ID)
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .deadline(ASSIGNMENT_DEADLINE)
        .file(FileV2.builder().id("file-id").build())
        .batch(Batch.builder().code(BATCH_CODE).build())
        .build();

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
        .assignment(assignment)
        .student(user)
        .point(0)
        .build();

    pageable = new PageRequest(0, 10);

    roomPage = new PageImpl<>(Collections.singletonList(room), pageable, 1);

    roomPointWebRequest = RoomPointWebRequest.builder().point(100).build();

    DATA_RESPONSE = RoomResponseMapper
        .toDataRoomWebResponse(this.room);

    PAGING_RESPONSE = RoomResponseMapper
        .toPagingRoomWebResponse(roomPage);

    BASE_RESPONSE = ResponseHelper.toBaseResponse(HttpStatus.OK);

    when(assignmentService.findAllRoomsByAssignmentId(ASSIGNMENT_ID, pageable))
        .thenReturn(roomPage);
    when(assignmentService.giveScoreToRoomByRoomId(ROOM_ID, MENTOR_SESSION_ID, 100))
        .thenReturn(room);
    when(assignmentService.findRoomById(ROOM_ID, ADMIN_SESSION_ID))
        .thenReturn(room);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(assignmentService);
  }

  @Test
  public void findAllRoomsByAssignmentId() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms")
            .cookie(cookies)
        .param("page", "1")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            pagingResponseJacksonTester.write(PAGING_RESPONSE)
            .getJson()));
    verify(assignmentService).findAllRoomsByAssignmentId(ASSIGNMENT_ID, pageable);
  }

  @Test
  public void findRoomById() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms/" + ROOM_ID)
            .cookie(cookies)
            .param("roomId", ROOM_ID))
        .andExpect(status().isOk())
        .andExpect(content().json(
            dataResponseJacksonTester.write(DATA_RESPONSE)
                .getJson()));
    verify(assignmentService).findRoomById(ROOM_ID, ADMIN_SESSION_ID);

  }

  @Test
  public void updateRoomScoreByMentor() throws Exception {
    super.setCookie(Role.MENTOR);
    mockMvc.perform(
        put("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms/" + ROOM_ID)
            .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(roomPointWebRequestJacksonTester.write(roomPointWebRequest).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().json(
            dataResponseJacksonTester.write(DATA_RESPONSE).getJson()));
    verify(assignmentService).giveScoreToRoomByRoomId(ROOM_ID, MENTOR_SESSION_ID, 100);
  }

  @Test
  public void updateRoomScoreByAdmin() throws Exception {
    super.setCookie(Role.ADMIN);
    mockMvc.perform(
        put("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms/" + ROOM_ID)
            .cookie(cookies)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(roomPointWebRequestJacksonTester.write(roomPointWebRequest).getJson()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void deleteRoomById() throws Exception {
    mockMvc.perform(
        delete("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID + "/rooms/" + ROOM_ID)
            .cookie(cookies))
        .andExpect(status().isOk())
        .andExpect(content().json(
            baseResponseJacksonTester.write(BASE_RESPONSE).getJson()));
    verify(assignmentService).deleteRoomById(ROOM_ID);
  }
}