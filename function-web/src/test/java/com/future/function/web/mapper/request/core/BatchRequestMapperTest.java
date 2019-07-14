package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.BatchWebRequest;
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
public class BatchRequestMapperTest {
  
  public static final String NAME = "name";
  
  public static final String CODE = "code";
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private BatchRequestMapper batchRequestMapper;
  
  public static final String ID = "id";
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenBatchWebRequestByParsingToBatchReturnBatchObject() {
    
    BatchWebRequest request = BatchWebRequest.builder()
      .id(ID)
      .name(NAME)
      .code(CODE)
      .build();
    when(validator.validate(request)).thenReturn(request);
    
    Batch batch = Batch.builder()
      .name(NAME)
      .code(CODE)
      .build();
    
    Batch parsedBatch = batchRequestMapper.toBatch(request);
    
    assertThat(parsedBatch).isEqualTo(batch);
    
    verify(validator).validate(request);
  }
  
  @Test
  public void testGivenIdAndBatchWebRequestByParsingToBatchReturnBatchObject() {
    
    BatchWebRequest request = BatchWebRequest.builder()
      .id(ID)
      .name(NAME)
      .code(CODE)
      .build();
    when(validator.validate(request)).thenReturn(request);
  
    Batch batch = Batch.builder()
      .id(ID)
      .name(NAME)
      .code(CODE)
      .build();
    
    Batch parsedBatch = batchRequestMapper.toBatch(ID, request);
    
    assertThat(parsedBatch).isEqualTo(batch);
    
    verify(validator).validate(request);
  }
  
}
