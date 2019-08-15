package com.future.function.repository.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TopicRepositoryTest {

  private static final String LOGGING_ROOM_ID = "loggingRoomId";

  private static final PageRequest PAGEABLE = new PageRequest(0, 2);

  private static final LoggingRoom LOGGING_ROOM = LoggingRoom.builder()
    .id(LOGGING_ROOM_ID)
    .build();

  private static final String TOPIC_ID_1 = "topicId1";

  private static final String TOPIC_ID_2 = "topicId2";

  @Autowired
  private TopicRepository topicRepository;

  @Before
  public void setUp() {

    Topic topic1 = Topic.builder()
      .id(TOPIC_ID_1)
      .loggingRoom(LOGGING_ROOM)
      .build();

    Topic topic2 = Topic.builder()
      .id(TOPIC_ID_2)
      .loggingRoom(LOGGING_ROOM)
      .build();

    topicRepository.save(topic1);
    topicRepository.save(topic2);
  }

  @After
  public void tearDown() {

    topicRepository.deleteAll();
  }

  @Test
  public void testByGivenLoggingRoomfindAllByLoggingRoomAndDeletedFalse() {

    Page<Topic> results = topicRepository.findAllByLoggingRoomAndDeletedFalse(
      LOGGING_ROOM, PAGEABLE);


    assertThat(results).isNotNull();
    assertThat(results.getContent()
                 .get(0)
                 .getId()).isEqualTo(TOPIC_ID_1);
    assertThat(results.getContent()
                 .get(1)
                 .getId()).isEqualTo(TOPIC_ID_2);
  }

}
