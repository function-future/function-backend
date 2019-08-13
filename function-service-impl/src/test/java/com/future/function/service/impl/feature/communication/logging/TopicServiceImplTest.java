package com.future.function.service.impl.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.repository.feature.communication.logging.TopicRepository;
import com.future.function.service.api.feature.communication.logging.LoggingRoomService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicServiceImplTest {

  private static final String LOGGING_ROOM_ID = "loggingRoomId";
  private static final PageRequest PAGEABLE = new PageRequest(0, 2);
  private static final LoggingRoom LOGGING_ROOM =
    LoggingRoom.builder().id(LOGGING_ROOM_ID).build();
  private static final String TOPIC_ID_1 = "topicId1";
  private static final String TOPIC_ID_2 = "topicId2";

  private Topic topic1;
  private Topic topic2;

  @Mock
  private TopicRepository topicRepository;

  @Mock
  private LoggingRoomService loggingRoomService;

  @InjectMocks
  private TopicServiceImpl topicService;

  @Before
  public void setUp() {
    topic1 = Topic.builder()
      .id(TOPIC_ID_1)
      .loggingRoom(LOGGING_ROOM)
      .build();

    topic2 = Topic.builder()
      .id(TOPIC_ID_2)
      .loggingRoom(LOGGING_ROOM)
      .build();
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(
      topicRepository,
      loggingRoomService
    );
  }

  @Test
  public void getTopicByLoggingRoom() {
    when(loggingRoomService.getLoggingRoom(LOGGING_ROOM_ID))
      .thenReturn(LOGGING_ROOM);
    when(topicRepository.findAllByLoggingRoomAndDeletedFalse(LOGGING_ROOM, PAGEABLE))
      .thenReturn(new PageImpl<>(Arrays.asList(topic1, topic2), PAGEABLE, 2));

    Page<Topic> result = topicService.getTopicByLoggingRoom(LOGGING_ROOM_ID, PAGEABLE);

    assertThat(result).isNotNull();
    assertThat(result.getContent().get(0).getId()).isEqualTo(TOPIC_ID_1);
    assertThat(result.getContent().get(1).getId()).isEqualTo(TOPIC_ID_2);

    verify(loggingRoomService).getLoggingRoom(LOGGING_ROOM_ID);
    verify(topicRepository).findAllByLoggingRoomAndDeletedFalse(LOGGING_ROOM, PAGEABLE);

  }

  @Test
  public void getTopic() {
    when(topicRepository.findOne(TOPIC_ID_1)).thenReturn(topic1);

    Topic result = topicService.getTopic(TOPIC_ID_1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(TOPIC_ID_1);

    verify(topicRepository).findOne(TOPIC_ID_1);
  }

  @Test
  public void createTopic() {
    when(loggingRoomService.getLoggingRoom(LOGGING_ROOM_ID))
      .thenReturn(LOGGING_ROOM);
    when(topicRepository.save(topic1)).thenReturn(topic1);

    Topic result = topicService.createTopic(topic1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(TOPIC_ID_1);

    verify(loggingRoomService).getLoggingRoom(LOGGING_ROOM_ID);
    verify(topicRepository).save(topic1);
  }

  @Test
  public void updateTopic() {
    when(topicRepository.findOne(TOPIC_ID_1))
      .thenReturn(topic1);
    when(topicRepository.save(topic1))
      .thenReturn(topic1);

    Topic result = topicService.updateTopic(topic1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(TOPIC_ID_1);

    verify(topicRepository).findOne(TOPIC_ID_1);
    verify(topicRepository).save(topic1);
  }

  @Test
  public void deleteTopic() {

    when(topicRepository.findOne(TOPIC_ID_1))
      .thenReturn(topic1);
    when(topicRepository.save(topic1))
      .thenReturn(topic1);

    topicService.deleteTopic(TOPIC_ID_1);

    assertThat(topic1.isDeleted()).isEqualTo(Boolean.TRUE);

    verify(topicRepository).findOne(TOPIC_ID_1);
    verify(topicRepository).save(topic1);
  }
}
