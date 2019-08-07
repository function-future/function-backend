package com.future.function.web.controller.communication.logging;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.logging.LogMessageService;
import com.future.function.service.api.feature.communication.logging.LoggingRoomService;
import com.future.function.service.api.feature.communication.logging.TopicService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.request.communication.logging.LogMessageRequestMapper;
import com.future.function.web.mapper.request.communication.logging.LoggingRoomRequestMapper;
import com.future.function.web.mapper.request.communication.logging.TopicRequestMapper;
import com.future.function.web.mapper.response.communication.logging.LoggingRoomResponseMapper;
import com.future.function.web.model.request.communication.logging.LogMessageWebRequest;
import com.future.function.web.model.request.communication.logging.LoggingRoomWebRequest;
import com.future.function.web.model.request.communication.logging.TopicWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.logging.LogMessageWebResponse;
import com.future.function.web.model.response.feature.communication.logging.LoggingRoomWebResponse;
import com.future.function.web.model.response.feature.communication.logging.TopicWebResponse;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author : Ricky Kennedy
 * Created At : 11:01 29/07/2019
 */
@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(LoggingRoomController.class)
public class LoggingRoomControllerTest extends TestHelper {

  private static final String URL_PREFIX = "urlPrefix";

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

  private static final String TOPIC_ID_1 = "topicId1";
  private static final String TOPIC_ID_2 = "topicId2";

  private static final Topic TOPIC1 = Topic.builder()
    .id(TOPIC_ID_1)
    .loggingRoom(LOGGING_ROOM1)
    .title(TITLE1)
    .build();

  private static final Topic TOPIC2 = Topic.builder()
    .id(TOPIC_ID_2)
    .loggingRoom(LOGGING_ROOM2)
    .title(TITLE2)
    .build();

  private static final Page<Topic> TOPIC_PAGE =
    new PageImpl<>(Arrays.asList(TOPIC1, TOPIC2), PAGEABLE,2);

  private static final String LOG_MESSAGE_ID_1 = "logMessageId1";
  private static final String LOG_MESSAGE_ID_2 = "logMessageId2";

  private LogMessage LOG_MESSAGE1 =
    LogMessage.builder()
    .id(LOG_MESSAGE_ID_1)
        .topic(TOPIC1)
        .sender(MEMBER)
        .text(DESCRIPTION)
        .build();

  private LogMessage LOG_MESSAGE2 =
    LogMessage.builder()
    .id(LOG_MESSAGE_ID_2)
        .topic(TOPIC1)
        .sender(MEMBER)
        .text(DESCRIPTION)
        .build();

  private Page<LogMessage> LOG_MESSAGE_PAGE =
    new PageImpl<>(Arrays.asList(LOG_MESSAGE1, LOG_MESSAGE2), PAGEABLE, 2);

  private static final String TEXT = "text";

  private static final LogMessageWebRequest LOG_MESSAGE_REQUEST =
    LogMessageWebRequest.builder()
      .text(TEXT)
      .build();

  private static final TopicWebRequest TOPIC_REQUEST =
    TopicWebRequest.builder()
      .title(TITLE1)
      .build();

  private static final LoggingRoomWebRequest LOGGING_ROOM_REQUEST =
    LoggingRoomWebRequest.builder()
      .title(TITLE1)
      .description(DESCRIPTION)
      .members(Arrays.asList(MEMBER_ID_1))
      .build();

  @MockBean
  private LoggingRoomService loggingRoomService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private LogMessageService logMessageService;

  @MockBean
  private LogMessageRequestMapper logMessageRequestMapper;

  @MockBean
  private TopicRequestMapper topicRequestMapper;

  @MockBean
  private LoggingRoomRequestMapper loggingRoomRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  private JacksonTester<LogMessageWebRequest> logMessageRequestJacksonTester;

  private JacksonTester<TopicWebRequest> topicRequestJacksonTester;

  private JacksonTester<LoggingRoomWebRequest> loggingRoomRequestJacksonTester;

  @Before
  public void setUp(){
    super.setUp();
    LOG_MESSAGE1.setCreatedAt(1L);
    LOG_MESSAGE2.setCreatedAt(2L);
  }


  @After
  public void tearDown()  {
    verifyNoMoreInteractions(
      loggingRoomService,
      topicService,
      logMessageService,
      logMessageRequestMapper,
      topicRequestMapper,
      loggingRoomRequestMapper);
  }

  @Test
  public void getLoggingRoomsByMember() throws Exception {
    super.setCookie(Role.ADMIN);

    when(loggingRoomService.getLoggingRoomsByMemberWithKeyword(eq(KEYWORD), any(String.class), eq(PAGEABLE)))
      .thenReturn(LOGGING_ROOM_PAGE);
    when(loggingRoomService.getLoggingRoomsByMember(any(String.class), eq(PAGEABLE)))
      .thenReturn(LOGGING_ROOM_PAGE);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<LoggingRoomWebResponse> response =
      LoggingRoomResponseMapper.toPagingLoggingRoomResponse(LOGGING_ROOM_PAGE, URL_PREFIX);

    mockMvc.perform(
      get("/api/communication/logging-rooms")
        .cookie(cookies).param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    mockMvc.perform(
      get("/api/communication/logging-rooms")
        .cookie(cookies).param("search", ""))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(fileProperties, times(2)).getUrlPrefix();

    verify(loggingRoomService).getLoggingRoomsByMember(any(String.class), eq(PAGEABLE));
    verify(loggingRoomService).getLoggingRoomsByMemberWithKeyword(eq(KEYWORD), any(String.class), eq(PAGEABLE));
  }

  @Test
  public void getLoggingRoomDetail() throws Exception{
    super.setCookie(Role.ADMIN);

    when(loggingRoomService.getLoggingRoom(LOGGING_ROOM_ID1))
      .thenReturn(LOGGING_ROOM1);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    DataResponse<LoggingRoomWebResponse> response =
      LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(LOGGING_ROOM1, URL_PREFIX);

    mockMvc.perform(
      get("/api/communication/logging-rooms/"
          +LOGGING_ROOM_ID1
      )
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(loggingRoomService).getLoggingRoom(LOGGING_ROOM_ID1);
  }

  @Test
  public void getLoggingRoomTopic() throws Exception{
    super.setCookie(Role.ADMIN);

    when(topicService.getTopicByLoggingRoom(LOGGING_ROOM_ID1, PAGEABLE))
      .thenReturn(TOPIC_PAGE);

    PagingResponse<TopicWebResponse> response =
      LoggingRoomResponseMapper.toPagingTopicResponse(
        TOPIC_PAGE
      );

    mockMvc.perform(
      get("/api/communication/logging-rooms/"
          +LOGGING_ROOM_ID1
          +"/topics"
      )
        .cookie(cookies).param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(topicService).getTopicByLoggingRoom(LOGGING_ROOM_ID1, PAGEABLE);
  }

  @Test
  public void getLoggingRoomTopicDetail() throws Exception {
    super.setCookie(Role.ADMIN);

    when(topicService.getTopic(TOPIC_ID_1))
      .thenReturn(TOPIC1);


    DataResponse<TopicWebResponse> response =
      LoggingRoomResponseMapper.toDataResponseTopicResponse(TOPIC1);

    mockMvc.perform(
      get("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
        +"/topics/"
        +TOPIC_ID_1
      )
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));


    verify(topicService).getTopic(TOPIC_ID_1);
  }

  @Test
  public void getLogMessages() throws Exception {
    super.setCookie(Role.ADMIN);

    when(logMessageService.getLogMessagesByTopic(TOPIC_ID_1, PAGEABLE))
      .thenReturn(LOG_MESSAGE_PAGE);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<LogMessageWebResponse> response =
      LoggingRoomResponseMapper.toPagingLogMessageResponse(
        LOG_MESSAGE_PAGE,
        URL_PREFIX
      );

    mockMvc.perform(
      get("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
        +"/topics/"
        +TOPIC_ID_1
        +"/log-messages"
      )
        .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));
    verify(fileProperties).getUrlPrefix();
    verify(logMessageService).getLogMessagesByTopic(TOPIC_ID_1, PAGEABLE);
  }

  @Test
  public void createLogMessage() throws Exception {
    super.setCookie(Role.STUDENT);

    when(logMessageRequestMapper.toLogMessage(
      eq(LOG_MESSAGE_REQUEST), any(String.class), eq(TOPIC_ID_1)))
      .thenReturn(LOG_MESSAGE1);

    when(logMessageService.createLogMessage(LOG_MESSAGE1))
      .thenReturn(LOG_MESSAGE1);

    mockMvc.perform(
      post("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
        +"/topics/"
        +TOPIC_ID_1
        +"/log-messages"
      )
      .cookie(cookies)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(logMessageRequestJacksonTester.write(LOG_MESSAGE_REQUEST).getJson()))
      .andExpect(status().isCreated());

    verify(logMessageService).createLogMessage(LOG_MESSAGE1);
    verify(logMessageRequestMapper).toLogMessage(
      eq(LOG_MESSAGE_REQUEST), any(String.class), eq(TOPIC_ID_1));
  }

  @Test
  public void createTopic() throws Exception {
    super.setCookie(Role.MENTOR);

    when(topicRequestMapper.toTopic(
      eq(TOPIC_REQUEST), any(String.class), any()))
      .thenReturn(TOPIC1);

    when(topicService.createTopic(TOPIC1))
      .thenReturn(TOPIC1);

    mockMvc.perform(
      post("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
        +"/topics"
      )
        .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(topicRequestJacksonTester.write(TOPIC_REQUEST).getJson()))
      .andExpect(status().isCreated());

    verify(topicService).createTopic(TOPIC1);
    verify(topicRequestMapper).toTopic(
      eq(TOPIC_REQUEST), any(String.class), any());
  }

  @Test
  public void createLoggingRoom() throws Exception {
    super.setCookie(Role.MENTOR);

    when(loggingRoomRequestMapper.toLoggingRoom(
      eq(LOGGING_ROOM_REQUEST), any()))
      .thenReturn(LOGGING_ROOM1);

    when(loggingRoomService.createLoggingRoom(LOGGING_ROOM1))
      .thenReturn(LOGGING_ROOM1);

    mockMvc.perform(
      post("/api/communication/logging-rooms"
      )
        .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(loggingRoomRequestJacksonTester.write(LOGGING_ROOM_REQUEST).getJson()))
      .andExpect(status().isCreated());

    verify(loggingRoomRequestMapper).toLoggingRoom(
      eq(LOGGING_ROOM_REQUEST), any());
    verify(loggingRoomService).createLoggingRoom(LOGGING_ROOM1);
  }

  @Test
  public void updateTopicDetail() throws Exception {
    super.setCookie(Role.MENTOR);

    when(topicRequestMapper.toTopic(
      TOPIC_REQUEST, LOGGING_ROOM_ID1, TOPIC_ID_1))
      .thenReturn(TOPIC1);

    when(topicService.updateTopic(TOPIC1))
      .thenReturn(TOPIC1);

    DataResponse<TopicWebResponse> response =
      LoggingRoomResponseMapper.toDataResponseTopicResponse(
        TOPIC1
      );

    mockMvc.perform(
      put("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
        +"/topics/"
        +TOPIC_ID_1
      )
        .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(topicRequestJacksonTester.write(TOPIC_REQUEST).getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(topicService).updateTopic(TOPIC1);
    verify(topicRequestMapper).toTopic(
      TOPIC_REQUEST, LOGGING_ROOM_ID1, TOPIC_ID_1);
  }

  @Test
  public void updateLoggingRoomDetail() throws Exception {
    super.setCookie(Role.MENTOR);

    when(loggingRoomRequestMapper.toLoggingRoom(
      LOGGING_ROOM_REQUEST, LOGGING_ROOM_ID1))
      .thenReturn(LOGGING_ROOM1);

    when(loggingRoomService.updateLoggingRoom(LOGGING_ROOM1))
      .thenReturn(LOGGING_ROOM1);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    DataResponse<LoggingRoomWebResponse> response =
      LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(
        LOGGING_ROOM1,
        URL_PREFIX
      );

    mockMvc.perform(
      put("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
      )
        .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(loggingRoomRequestJacksonTester.write(LOGGING_ROOM_REQUEST).getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(fileProperties).getUrlPrefix();

    verify(loggingRoomService).updateLoggingRoom(LOGGING_ROOM1);
    verify(loggingRoomRequestMapper).toLoggingRoom(
      LOGGING_ROOM_REQUEST, LOGGING_ROOM_ID1);
  }

  @Test
  public void deleteTopic() throws Exception {
    super.setCookie(Role.MENTOR);
    doNothing().when(topicService).deleteTopic(TOPIC_ID_1);

    mockMvc.perform(
      delete("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1
        +"/topics/"
        +TOPIC_ID_1
      )
        .cookie(cookies))
      .andExpect(status().isOk());

    verify(topicService).deleteTopic(TOPIC_ID_1);
  }

  @Test
  public void deleteLoggingRoom() throws Exception {
    super.setCookie(Role.MENTOR);

    doNothing().when(loggingRoomService).deleteLoggingRoom(LOGGING_ROOM_ID1);

    mockMvc.perform(
      delete("/api/communication/logging-rooms/"
        +LOGGING_ROOM_ID1).cookie(cookies))
      .andExpect(status().isOk());

    verify(loggingRoomService).deleteLoggingRoom(LOGGING_ROOM_ID1);
  }
}
