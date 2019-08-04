package com.future.function.web.mapper.response.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import com.future.function.web.model.response.feature.scoring.AssignmentWebResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomResponseMapperTest {
  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String BATCH_CODE = "batch-code";
  private static final String FILE_URl = "file-url";
  private static final String FILE_ID = "file-id";
  private static final String ROOM_ID = "room-id";
  private static final String USER_ID = "user-id";
  private Paging paging;
  private Pageable pageable;
  private User student;
  private Assignment assignment;
  private Room room;
  private Batch batch;
  private FileV2 fileV2;
  private Page<Room> roomPage;
  private AssignmentWebResponse assignmentWebResponse;
  private RoomWebResponse roomWebResponse;
  private BatchWebResponse batchWebResponse;
  private UserWebResponse userWebResponse;
  private DataResponse<RoomWebResponse> roomWebResponseDataResponse;
  private PagingResponse<RoomWebResponse> roomWebResponsePagingResponse;

  @Before
  public void setUp() throws Exception {
    batch = Batch.builder().code(BATCH_CODE).build();
    fileV2 = FileV2.builder().id(FILE_ID).fileUrl(FILE_URl).build();
    assignment = Assignment
        .builder()
        .id(null)
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .deadline(ASSIGNMENT_DEADLINE)
        .batch(batch)
        .file(fileV2)
        .build();
    assignment.setCreatedAt(ASSIGNMENT_DEADLINE);
    assignmentWebResponse = AssignmentWebResponse
        .builder()
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .deadline(ASSIGNMENT_DEADLINE)
        .batchCode(BATCH_CODE)
        .file(FILE_URl)
            .fileId(FILE_ID)
        .build();
    assignmentWebResponse.setUploadedDate(ASSIGNMENT_DEADLINE);
    batchWebResponse = BatchWebResponse.builder()
        .code(BATCH_CODE).build();
    student = User.builder().id(USER_ID).name("name").address("address").batch(batch).email("email")
        .password("password").phone("phone").pictureV2(fileV2).role(Role.STUDENT).build();
    batchWebResponse = new BatchWebResponse();
    BeanUtils.copyProperties(batch, batchWebResponse);
    userWebResponse = new UserWebResponse();
    BeanUtils.copyProperties(student, userWebResponse);
    userWebResponse.setBatch(batchWebResponse);
    userWebResponse.setAvatar(FILE_URl);
    userWebResponse.setAvatarId(fileV2.getId());
    userWebResponse.setRole("STUDENT");
    room = Room.builder().assignment(assignment).student(student).point(0).build();
    roomWebResponse = RoomWebResponse.builder().assignment(assignmentWebResponse).student(userWebResponse)
        .point(0).build();
    roomWebResponseDataResponse = DataResponse
        .<RoomWebResponse>builder()
        .data(roomWebResponse)
        .code(HttpStatus.OK.value())
        .status(ResponseHelper.toProperStatusFormat(HttpStatus.OK.getReasonPhrase()))
        .build();
    pageable = new PageRequest(0, 10);
    roomPage = new PageImpl<>(Collections.singletonList(room), pageable, 10);
    paging = Paging
        .builder()
        .page(roomPage.getNumber())
        .size(roomPage.getSize())
        .totalRecords(roomPage.getTotalElements())
        .build();
    roomWebResponsePagingResponse = PagingResponse
        .<RoomWebResponse>builder()
        .data(Collections.singletonList(roomWebResponse))
        .code(HttpStatus.OK.value())
        .status(ResponseHelper.toProperStatusFormat(HttpStatus.OK.getReasonPhrase()))
        .paging(paging)
        .build();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void toDataRoomWebResponse() {
    DataResponse<RoomWebResponse> actual = RoomResponseMapper.toDataRoomWebResponse(room);
    assertThat(actual.getData().getAssignment()).isEqualTo(assignmentWebResponse);
    assertThat(actual.getData().getStudent()).isEqualTo(userWebResponse);
    assertThat(actual.getData().getPoint()).isEqualTo(0);
  }

  @Test
  public void toPagingRoomWebResponse() {
    PagingResponse<RoomWebResponse> actual = RoomResponseMapper.toPagingRoomWebResponse(roomPage);
    assertThat(actual.getData().get(0).getAssignment()).isEqualTo(assignmentWebResponse);
    assertThat(actual.getData().get(0).getStudent()).isEqualTo(userWebResponse);
    assertThat(actual.getData().get(0).getPoint()).isEqualTo(0);
  }
}
