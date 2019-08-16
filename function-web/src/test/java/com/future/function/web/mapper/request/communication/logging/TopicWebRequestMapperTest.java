package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.TopicWebRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicWebRequestMapperTest {

  private static final String TITLE = "title";

  private static final String LOGGING_ROOM_ID = "loggingRoomId";

  private static final String TOPIC_ID = "topicId";

  private static final TopicWebRequest TOPIC_REQUEST = TopicWebRequest.builder()
    .title(TITLE)
    .build();

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private TopicRequestMapper topicRequestMapper;

  @Test
  public void toTopic() {

    when(validator.validate(TOPIC_REQUEST)).thenReturn(TOPIC_REQUEST);

    Topic data = topicRequestMapper.toTopic(
      TOPIC_REQUEST, LOGGING_ROOM_ID, TOPIC_ID);

    assertThat(data.getId()).isEqualTo(TOPIC_ID);
    assertThat(data.getLoggingRoom()
                 .getId()).isEqualTo(LOGGING_ROOM_ID);
    assertThat(data.getTitle()).isEqualTo(TITLE);

    verify(validator).validate(TOPIC_REQUEST);
  }

}
