package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LogMessageWebRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogMessageWebRequestMapperTest {

  private static final String DESCRIPTION = "description";

  private static final String MEMBER_ID = "memberId";

  private static final String TOPIC_ID = "topicId";

  private static final LogMessageWebRequest LOG_MESSAGE_REQUEST =
    LogMessageWebRequest.builder()
      .text(DESCRIPTION)
      .build();

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private LogMessageRequestMapper logMessageRequestMapper;

  @Test
  public void toLogMessage() {
    when(validator.validate(LOG_MESSAGE_REQUEST))
      .thenReturn(LOG_MESSAGE_REQUEST);

    LogMessage result =
      logMessageRequestMapper.toLogMessage(LOG_MESSAGE_REQUEST, MEMBER_ID, TOPIC_ID);

    assertThat(result.getTopic().getId()).isEqualTo(TOPIC_ID);
    assertThat(result.getSender().getId()).isEqualTo(MEMBER_ID);

    verify(validator).validate(LOG_MESSAGE_REQUEST);

  }
}
