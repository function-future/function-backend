package com.future.function.service.impl.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.logging.LogMessageRepository;
import com.future.function.service.api.feature.communication.logging.TopicService;
import com.future.function.service.api.feature.core.UserService;
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
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogMessageServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String TOPIC_ID = "topicId";

  private static final String LOG_MESSAGE_ID_1 = "logMessageId1";

  private static final String LOG_MESSAGE_ID_2 = "logMessageId2";

  private static final Topic TOPIC = Topic.builder()
    .id(TOPIC_ID)
    .build();

  private static final String SENDER_ID = "senderId";

  private static final User SENDER = User.builder()
    .id(SENDER_ID)
    .build();

  private LogMessage logMessage1;

  private LogMessage logMessage2;

  @Mock
  private TopicService topicService;

  @Mock
  private UserService userService;

  @Mock
  private LogMessageRepository logMessageRepository;

  @InjectMocks
  private LogMessageServiceImpl logMessageService;

  @Before
  public void setUp() {

    logMessage1 = LogMessage.builder()
      .id(LOG_MESSAGE_ID_1)
      .topic(TOPIC)
      .sender(SENDER)
      .build();
    logMessage1.setCreatedAt(1L);

    logMessage2 = LogMessage.builder()
      .id(LOG_MESSAGE_ID_2)
      .topic(TOPIC)
      .build();
    logMessage2.setCreatedAt(2L);

  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(userService, topicService, logMessageRepository);
  }

  @Test
  public void getLogMessagesByTopic() {

    when(topicService.getTopic(TOPIC_ID)).thenReturn(TOPIC);
    when(logMessageRepository.findAllByTopicOrderByCreatedAtDesc(TOPIC,
                                                                 PAGEABLE
    )).thenReturn(
      new PageImpl<>(Arrays.asList(logMessage2, logMessage1), PAGEABLE, 2));

    Page<LogMessage> results = logMessageService.getLogMessagesByTopic(
      TOPIC_ID, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent()
                 .get(0)
                 .getId()).isEqualTo(LOG_MESSAGE_ID_2);
    assertThat(results.getContent()
                 .get(1)
                 .getId()).isEqualTo(LOG_MESSAGE_ID_1);

    verify(topicService).getTopic(TOPIC_ID);
    verify(logMessageRepository).findAllByTopicOrderByCreatedAtDesc(TOPIC,
                                                                    PAGEABLE
    );
  }

  @Test
  public void createLogMessage() {

    when(userService.getUser(SENDER_ID)).thenReturn(SENDER);
    when(topicService.getTopic(TOPIC_ID)).thenReturn(TOPIC);
    when(logMessageRepository.save(logMessage1)).thenReturn(logMessage1);

    LogMessage result = logMessageService.createLogMessage(logMessage1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(LOG_MESSAGE_ID_1);

    verify(userService).getUser(SENDER_ID);
    verify(topicService).getTopic(TOPIC_ID);
    verify(logMessageRepository).save(logMessage1);
  }

}
