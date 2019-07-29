package com.future.function.web.controller.communication.logging;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.logging.LoggingRoomService;
import com.future.function.web.TestHelper;
import com.future.function.web.mapper.response.communication.logging.LoggingRoomResponseMapper;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.logging.LoggingRoomResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author : Ricky Kennedy
 * Created At : 11:01 29/07/2019
 */
public class LoggingRoomControllerTest extends TestHelper {

  private static final PageRequest PAGEABLE = new PageRequest(0, 10);

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member2";

  private static final String BATCH_ID = "batchId";

  private static final String BATCH_NAME = "batchName";

  private static final String BATCH_CODE = "batchCode";

  private static final String UNIVERSITY = "itb";

  private static final String KEYWORD = "keyword";

  private static final String DESCRIPTION = "description";
  private static final String LOGGING_ROOM_ID1 = "loggingRoomId1";
  private static final String LOGGING_ROOM_ID2 = "loggingRoomId2";
  private static final String TITLE1 = "title1";
  private static final String TITLE2 = "title2";

  private static final Batch BATCH =
    Batch.builder()
      .id(BATCH_ID)
      .name(BATCH_NAME)
      .code(BATCH_CODE)
      .build();

  private static final User MEMBER = User.builder()
    .id(MEMBER_ID_1)
    .name(MEMBER_NAME_1)
    .pictureV2(FileV2.builder().thumbnailUrl(THUMBNAIL_URL).build())
    .batch(BATCH)
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final User MEMBER2 = User.builder()
    .id(MEMBER_ID_2)
    .name(MEMBER_NAME_2)
    .pictureV2(FileV2.builder().thumbnailUrl(THUMBNAIL_URL).build())
    .batch(BATCH)
    .role(Role.STUDENT)
    .build();

  private static final LoggingRoom LOGGING_ROOM1 =
    LoggingRoom.builder()
      .id(LOGGING_ROOM_ID1)
      .title(TITLE1)
      .members(Arrays.asList(MEMBER))
      .description(DESCRIPTION)
      .build();

  private static final LoggingRoom LOGGING_ROOM2 =
    LoggingRoom.builder()
      .id(LOGGING_ROOM_ID2)
      .title(TITLE2)
      .members(Arrays.asList(MEMBER2))
      .description(DESCRIPTION)
      .build();

  private static final Page<LoggingRoom> LOGGING_ROOM_PAGE =
    new PageImpl<>(Arrays.asList(LOGGING_ROOM1, LOGGING_ROOM2), PAGEABLE, 2);


  @MockBean
  private LoggingRoomService loggingRoomService;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void getLoggingRoomsByMember() throws Exception {
    when(loggingRoomService.getLoggingRoomsByMemberWithKeyword(KEYWORD, MEMBER_ID_1, PAGEABLE))
      .thenReturn(LOGGING_ROOM_PAGE);
    when(loggingRoomService.getLoggingRoomsByMember(MEMBER_ID_1, PAGEABLE))
      .thenReturn(LOGGING_ROOM_PAGE);

    PagingResponse<LoggingRoomResponse> response =
      LoggingRoomResponseMapper.toPagingLoggingRoomResponse(LOGGING_ROOM_PAGE);

    mockMvc.perform(
      get("/api/communication/logging-rooms")
        .cookie(cookies).param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    mockMvc.perform(
      get("/api/communication/logging-rooms")
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(loggingRoomService).getLoggingRoomsByMember(MEMBER_ID_1, PAGEABLE);
    verify(loggingRoomService).getLoggingRoomsByMemberWithKeyword(KEYWORD, MEMBER_ID_1, PAGEABLE);
  }

  @Test
  public void getLoggingRoomDetail() {
  }

  @Test
  public void getLoggingRoomTopic() {
  }

  @Test
  public void getLoggingRoomTopicDetail() {
  }

  @Test
  public void getLogMessages() {
  }

  @Test
  public void createLogMessage() {
  }

  @Test
  public void createTopic() {
  }

  @Test
  public void createLoggingRoom() {
  }

  @Test
  public void updateTopicDetail() {
  }

  @Test
  public void updateLoggingRoomDetail() {
  }

  @Test
  public void deleteTopic() {
  }

  @Test
  public void deleteLoggingRoom() {
  }
}