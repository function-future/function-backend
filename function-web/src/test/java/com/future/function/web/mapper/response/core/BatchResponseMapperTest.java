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

  private static final DataResponse<BatchWebResponse>
    CREATED_BATCH_DATA_RESPONSE = DataResponse.<BatchWebResponse>builder().code(
    HttpStatus.CREATED.value())
    .status("CREATED")
    .data(BATCH_WEB_RESPONSE_1)
    .build();

  private static final DataResponse<BatchWebResponse>
    RETRIEVED_BATCH_DATA_RESPONSE =
    DataResponse.<BatchWebResponse>builder().code(HttpStatus.OK.value())
      .status("OK")
      .data(BATCH_WEB_RESPONSE_1)
      .build();

  private static final DataResponse<List<BatchWebResponse>>
    BATCH_WEB_RESPONSE_DATA_RESPONSE_LIST =
    DataResponse.<List<BatchWebResponse>>builder().code(200)
      .status("OK")
      .data(BATCH_WEB_RESPONSES)
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
    assertThat(dataResponse).isEqualTo(RETRIEVED_BATCH_DATA_RESPONSE);
  }

  @Test
  public void testGivenHttpStatusAndBatchDataByMappingToDataResponseReturnDataResponseObject() {

    DataResponse<BatchWebResponse> dataResponse =
      BatchResponseMapper.toBatchDataResponse(HttpStatus.CREATED, BATCH_1);

    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(CREATED_BATCH_DATA_RESPONSE);
  }

  @Test
  public void testGivenBatchesDataByMappingToPagingResponseReturnPagingResponseObject() {

    DataResponse<List<BatchWebResponse>> pagingResponse =
      BatchResponseMapper.toBatchesDataResponse(BATCHES);

    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(BATCH_WEB_RESPONSE_DATA_RESPONSE_LIST);
  }

  @Test
  public void testGivenBatchByMappingToBatchWebResponseReturnBatchWebResponseObject() {

    BatchWebResponse batchWebResponse = BatchResponseMapper.toBatchWebResponse(
      BATCH_1);

    assertThat(batchWebResponse).isNotNull();
    assertThat(batchWebResponse).isEqualTo(BATCH_WEB_RESPONSE_1);
  }

}
