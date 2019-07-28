package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LoggingRoomRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Author : Ricky Kennedy
 * Created At : 23:07 28/07/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggingRoomRequestMapperTest {

  private static final String DESCRIPTION = "description";

  private static final String TITLE = "title";

  private static final String MEMBER_ID = "memberId";

  private static final String LOGGING_ROOM_ID = "loggingRoomId";

  private static final LoggingRoomRequest LOGGING_ROOM_REQUEST=
    LoggingRoomRequest.builder()
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
    when(validator.validate(LOGGING_ROOM_REQUEST))
      .thenReturn(LOGGING_ROOM_REQUEST);

    LoggingRoom data = loggingRoomRequestMapper.toLoggingRoom(
      LOGGING_ROOM_REQUEST, LOGGING_ROOM_ID
    );

    assertThat(data.getId()).isEqualTo(LOGGING_ROOM_ID);

    verifyZeroInteractions(validator);
  }
}