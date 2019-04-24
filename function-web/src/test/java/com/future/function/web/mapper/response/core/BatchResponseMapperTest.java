package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchResponseMapperTest {
  
  private static final Batch BATCH_1 = new Batch(1L);
  
  private static final Batch BATCH_2 = new Batch(2L);
  
  private static final Batch BATCH_3 = new Batch(3L);
  
  private static final BatchWebResponse BATCH_WEB_RESPONSE =
    new BatchWebResponse(1L);
  
  private static final List<Long> BATCH_NUMBERS = Arrays.asList(1L, 2L, 3L);
  
  private static final List<Batch> BATCHES = Arrays.asList(
    BATCH_1, BATCH_2, BATCH_3);
  
  private static final DataResponse<BatchWebResponse> BATCH_DATA_RESPONSE =
    DataResponse.<BatchWebResponse>builder().code(HttpStatus.CREATED.value())
      .status("CREATED")
      .data(BATCH_WEB_RESPONSE)
      .build();
  
  private static final DataResponse<List<Long>> BATCH_NUMBERS_DATA_RESPONSE =
    DataResponse.<List<Long>>builder().code(HttpStatus.OK.value())
      .status("OK")
      .data(BATCH_NUMBERS)
      .build();
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenBatchDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<BatchWebResponse> dataResponse =
      BatchResponseMapper.toBatchDataResponse(BATCH_1);
    
    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(BATCH_DATA_RESPONSE);
  }
  
  @Test
  public void testGivenBatchesDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<List<Long>> dataResponse =
      BatchResponseMapper.toBatchesDataResponse(BATCHES);
    
    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(BATCH_NUMBERS_DATA_RESPONSE);
  }
  
}