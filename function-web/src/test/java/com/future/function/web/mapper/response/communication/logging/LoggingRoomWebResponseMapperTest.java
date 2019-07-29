package com.future.function.web.mapper.response.communication.logging;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.logging.LogMessageWebResponse;
import com.future.function.web.model.response.feature.communication.logging.LoggingRoomWebResponse;
import com.future.function.web.model.response.feature.communication.logging.TopicWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author : Ricky Kennedy
 * Created At : 22:13 28/07/2019
 */
public class LoggingRoomWebResponseMapperTest {

  private static final String DESCRIPTION = "description";
  private static final String LOGGING_ROOM_ID1 = "loggingRoomId1";
  private static final String LOGGING_ROOM_ID2 = "loggingRoomId2";
  private static final String TITLE1 = "title1";
  private static final String TITLE2 = "title2";
  private LoggingRoom loggingRoom1;
  private LoggingRoom loggingRoom2;

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member2";

  private static final String BATCH_ID = "batchId";

  private static final String BATCH_NAME = "batchName";

  private static final String BATCH_CODE = "batchCode";

  private static final String UNIVERSITY = "itb";

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

  private static final String TOPIC_ID_1 = "topicId1";
  private static final String TOPIC_ID_2 = "topicId2";

  private Topic topic1;
  private Topic topic2;

  private LogMessage logMessage1;
  private LogMessage logMessage2;

  private static final String LOG_MESSAGE_ID_1 = "logMessageId1";
  private static final String LOG_MESSAGE_ID_2 = "logMessageId2";

  @Before
  public void setUp() throws Exception {
    loggingRoom1 =
      LoggingRoom.builder()
        .id(LOGGING_ROOM_ID1)
        .title(TITLE1)
        .members(Arrays.asList(MEMBER))
        .description(DESCRIPTION)
        .build();
    loggingRoom1.setCreatedAt(1L);

    loggingRoom2 =
      LoggingRoom.builder()
        .id(LOGGING_ROOM_ID2)
        .title(TITLE2)
        .members(Arrays.asList(MEMBER2))
        .description(DESCRIPTION)
        .build();
    loggingRoom2.setCreatedAt(2L);

    topic1 = Topic.builder()
      .id(TOPIC_ID_1)
      .loggingRoom(loggingRoom1)
      .title(TITLE1)
      .build();

    topic2 = Topic.builder()
      .id(TOPIC_ID_2)
      .loggingRoom(loggingRoom1)
      .title(TITLE2)
      .build();

    logMessage1 =
      LogMessage.builder()
        .id(LOG_MESSAGE_ID_1)
        .topic(topic1)
        .sender(MEMBER)
        .text(DESCRIPTION)
        .build();
    logMessage1.setCreatedAt(1L);

    logMessage2 =
      LogMessage.builder()
        .id(LOG_MESSAGE_ID_2)
        .topic(topic1)
        .sender(MEMBER)
        .text(DESCRIPTION)
        .build();
    logMessage2.setCreatedAt(2L);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void toPagingLoggingRoomResponse() {
    PagingResponse<LoggingRoomWebResponse> data =
      LoggingRoomResponseMapper.toPagingLoggingRoomResponse(
        new PageImpl<>(Arrays.asList(loggingRoom1, loggingRoom2),
          PageHelper.toPageable(1,2), 2)
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);
    assertThat(data.getData().get(1).getId()).isEqualTo(LOGGING_ROOM_ID2);
  }

  @Test
  public void toDataResponseLoggingRoomResponse() {
    DataResponse<LoggingRoomWebResponse> data = LoggingRoomResponseMapper.toDataResponseLoggingRoomResponse(
      loggingRoom1
    );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getId()).isEqualTo(LOGGING_ROOM_ID1);
  }

  @Test
  public void toPagingTopicResponse() {
    PagingResponse<TopicWebResponse> data =
      LoggingRoomResponseMapper.toPagingTopicResponse(
        new PageImpl<>(Arrays.asList(topic1, topic2),
          PageHelper.toPageable(1,2), 2)
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(TOPIC_ID_1);
    assertThat(data.getData().get(1).getId()).isEqualTo(TOPIC_ID_2);
  }

  @Test
  public void toDataResponseTopicResponse() {
    DataResponse<TopicWebResponse> data =
      LoggingRoomResponseMapper.toDataResponseTopicResponse(
        topic1
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getId()).isEqualTo(TOPIC_ID_1);
  }

  @Test
  public void toPagingLogMessageResponse() {
    PagingResponse<LogMessageWebResponse> data =
      LoggingRoomResponseMapper.toPagingLogMessageResponse(
        new PageImpl<>(Arrays.asList(logMessage1, logMessage2),
          PageHelper.toPageable(1,2), 2)
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(LOG_MESSAGE_ID_1);
    assertThat(data.getData().get(1).getId()).isEqualTo(LOG_MESSAGE_ID_2);
  }
}
