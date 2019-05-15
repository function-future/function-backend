package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchResponseMapperTest {
  
  private static final Batch BATCH_1 = new Batch("id-1", "name-1", "1");
  
  private static final Batch BATCH_2 = new Batch("id-2", "name-2", "2");
  
  private static final Batch BATCH_3 = new Batch("id-3", "name-3", "3");
  
  private static final BatchWebResponse BATCH_WEB_RESPONSE_1 =
    BatchWebResponse.builder()
      .id("id-1")
      .name("name-1")
      .code("1")
      .build();
  
  private static final BatchWebResponse BATCH_WEB_RESPONSE_2 =
    BatchWebResponse.builder()
      .id("id-2")
      .name("name-2")
      .code("2")
      .build();
  
  private static final BatchWebResponse BATCH_WEB_RESPONSE_3 =
    BatchWebResponse.builder()
      .id("id-3")
      .name("name-3")
      .code("3")
      .build();
  
  private static final List<BatchWebResponse> BATCH_WEB_RESPONSES =
    Arrays.asList(
      BATCH_WEB_RESPONSE_1, BATCH_WEB_RESPONSE_2, BATCH_WEB_RESPONSE_3);
  
  private static final List<Batch> BATCHES = Arrays.asList(
    BATCH_1, BATCH_2, BATCH_3);
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  private static final Page<Batch> BATCH_PAGE = new PageImpl<>(
    BATCHES, PAGEABLE, BATCHES.size());
  
  private static final DataResponse<BatchWebResponse> BATCH_DATA_RESPONSE =
    DataResponse.<BatchWebResponse>builder().code(HttpStatus.CREATED.value())
      .status("CREATED")
      .data(BATCH_WEB_RESPONSE_1)
      .build();
  
  private static final PagingResponse<BatchWebResponse>
    BATCH_WEB_RESPONSE_PAGING_RESPONSE =
    PagingResponse.<BatchWebResponse>builder().code(200)
      .status("OK")
      .data(BATCH_WEB_RESPONSES)
      .paging(PageHelper.toPaging(BATCH_PAGE))
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
  public void testGivenBatchesDataByMappingToPagingResponseReturnPagingResponseObject() {
    
    PagingResponse<BatchWebResponse> pagingResponse =
      BatchResponseMapper.toBatchesPagingResponse(BATCH_PAGE);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(BATCH_WEB_RESPONSE_PAGING_RESPONSE);
  }
  
}