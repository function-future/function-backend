package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LoggingRoomWebRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingRoomWebRequestMapperTest {

  private static final String DESCRIPTION = "description";

  private static final String TITLE = "title";

  private static final String MEMBER_ID = "memberId";

  private static final String LOGGING_ROOM_ID = "loggingRoomId";

  private static final LoggingRoomWebRequest LOGGING_ROOM_REQUEST =
    LoggingRoomWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .members(Arrays.asList(MEMBER_ID))
      .build();

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private LoggingRoomRequestMapper loggingRoomRequestMapper;

  @Test
  public void toLoggingRoom() {

    when(validator.validate(LOGGING_ROOM_REQUEST)).thenReturn(
      LOGGING_ROOM_REQUEST);

    LoggingRoom data = loggingRoomRequestMapper.toLoggingRoom(
      LOGGING_ROOM_REQUEST, LOGGING_ROOM_ID);

    assertThat(data.getId()).isEqualTo(LOGGING_ROOM_ID);

    verify(validator).validate(LOGGING_ROOM_REQUEST);
  }

}
