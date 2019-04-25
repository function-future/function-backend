package com.future.function.web.mapper.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import com.future.function.web.dummy.data.DummyData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebRequestMapperTest {
  
  private static final String VALID_JSON = "{\"number\":1,\"email\":\"email\"}";
  
  private static final String INVALID_JSON = "{}";
  
  private static final DummyData DUMMY_DATA = new DummyData(1, "email");
  
  @Mock
  private ObjectMapper objectMapper;
  
  @InjectMocks
  private WebRequestMapper requestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(objectMapper);
  }
  
  @Test
  public void testGivenValidJsonByParsingToDummyDataClassReturnDummyDataObject()
    throws Exception {
    
    when(objectMapper.readValue(VALID_JSON, DummyData.class)).thenReturn(
      DUMMY_DATA);
    
    assertThat(
      requestMapper.toWebRequestObject(VALID_JSON, DummyData.class)).isEqualTo(
      DUMMY_DATA);
    
    verify(objectMapper).readValue(VALID_JSON, DummyData.class);
  }
  
  @Test
  public void testGivenInvalidJsonByParsingToDummyDataClassReturnBadRequestException()
    throws Exception {
    
    when(objectMapper.readValue(INVALID_JSON, DummyData.class)).thenThrow(
      new IOException());
    
    catchException(
      () -> requestMapper.toWebRequestObject(INVALID_JSON, DummyData.class));
    
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Bad Request");
    
    verify(objectMapper).readValue(INVALID_JSON, DummyData.class);
  }
  
}
