package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Batch;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchRequestMapperTest {
  
  @Mock
  private ObjectValidator validator;
  
  @InjectMocks
  private BatchRequestMapper batchRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenBatchWebRequestByParsingToBatchReturnBatchObject() {
    
    BatchWebRequest request = BatchWebRequest.builder()
      .name("name")
      .code("code")
      .build();
    when(validator.validate(request)).thenReturn(request);
    
    Batch batch = Batch.builder()
      .name("name")
      .code("code")
      .build();
    
    Batch parsedBatch = batchRequestMapper.toBatch(request);
    
    assertThat(parsedBatch).isEqualTo(batch);
    
    verify(validator).validate(request);
  }
  
  @Test
  public void testGivenIdAndBatchWebRequestByParsingToBatchReturnBatchObject() {
    
    BatchWebRequest request = BatchWebRequest.builder()
      .name("name")
      .code("code")
      .build();
    when(validator.validate(request)).thenReturn(request);
    
    String id = "id";
    Batch batch = Batch.builder()
      .id(id)
      .name("name")
      .code("code")
      .build();
    
    Batch parsedBatch = batchRequestMapper.toBatch(id, request);
    
    assertThat(parsedBatch).isEqualTo(batch);
    
    verify(validator).validate(request);
  }
  
}
