package com.future.function.repository.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
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
public class LogMessageRepositoryTest {

  private static final String TOPIC_ID = "topicId";

  private static final String LOG_MESSAGE_ID_1 = "logMessageId1";

  private static final String LOG_MESSAGE_ID_2 = "logMessageId2";

  private static final PageRequest PAGEABLE = new PageRequest(0, 2);

  private static final Topic TOPIC = Topic.builder()
    .id(TOPIC_ID)
    .build();

  @Autowired
  private LogMessageRepository logMessageRepository;

  {
  }

  @Before
  public void setUp() {

    LogMessage logMessage1 = LogMessage.builder()
      .id(LOG_MESSAGE_ID_1)
      .topic(TOPIC)
      .build();
    logMessage1.setCreatedAt(1L);

    LogMessage logMessage2 = LogMessage.builder()
      .id(LOG_MESSAGE_ID_2)
      .topic(TOPIC)
      .build();
    logMessage2.setCreatedAt(2L);

    logMessageRepository.save(logMessage1);
    logMessageRepository.save(logMessage2);
  }

  @After
  public void tearDown() {

    logMessageRepository.deleteAll();
  }

  @Test
  public void testGivenTopicByFindingAllLogMessagesReturnPagedLogMessages() {

    Page<LogMessage> results =
      logMessageRepository.findAllByTopicOrderByCreatedAtDesc(TOPIC, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent()
                 .get(0)
                 .getId()).isEqualTo(LOG_MESSAGE_ID_2);
    assertThat(results.getContent()
                 .get(1)
                 .getId()).isEqualTo(LOG_MESSAGE_ID_1);
  }
}
