package com.future.function.web.mapper.request.scoring;

import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.RoomPointWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomRequestMapperTest {

  private static final Integer POINT = 100;

  private RoomPointWebRequest request;

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private RoomRequestMapper requestMapper;

  @Before
  public void setUp() throws Exception {
    request = RoomPointWebRequest.builder()
        .point(POINT)
        .build();
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testValidate() {
    when(validator.validate(request)).thenReturn(request);
    RoomPointWebRequest actual = requestMapper.validate(request);
    assertThat(actual).isEqualTo(request);
    verify(validator).validate(request);
  }
}
