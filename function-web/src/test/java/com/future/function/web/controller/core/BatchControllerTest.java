package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.BatchRequestMapper;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.request.core.BatchWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = BatchController.class)
public class BatchControllerTest extends TestHelper {

  private static final String FIRST_BATCH_ID = "id-1";

  private static final String FIRST_BATCH_CODE = "1";

  private static final String FIRST_BATCH_NAME = "name-1";

  private static final String SECOND_BATCH_ID = "id-2";

  private static final String SECOND_BATCH_CODE = "2";

  private static final String SECOND_BATCH_NAME = "name-2";

  private static final Batch FIRST_BATCH = Batch.builder()
    .id(FIRST_BATCH_ID)
    .name(FIRST_BATCH_NAME)
    .code(FIRST_BATCH_CODE)
    .build();

  private static final Batch SECOND_BATCH = Batch.builder()
    .id(SECOND_BATCH_ID)
    .name(SECOND_BATCH_NAME)
    .code(SECOND_BATCH_CODE)
    .build();

  private static final List<Batch> BATCHES = Arrays.asList(
    FIRST_BATCH, SECOND_BATCH);

  private static final DataResponse<List<BatchWebResponse>>
    BATCHES_DATA_RESPONSE = BatchResponseMapper.toBatchesDataResponse(
    BATCHES);

  private static final DataResponse<BatchWebResponse> RETRIEVED_DATA_RESPONSE =
    BatchResponseMapper.toBatchDataResponse(FIRST_BATCH);

  private static final DataResponse<BatchWebResponse> CREATED_DATA_RESPONSE =
    BatchResponseMapper.toBatchDataResponse(HttpStatus.CREATED, FIRST_BATCH);

  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);

  private BatchWebRequest batchWebRequest;

  private JacksonTester<BatchWebRequest> batchWebRequestJacksonTester;

  @MockBean
  private BatchService batchService;

  @MockBean
  private BatchRequestMapper batchRequestMapper;

  @Override
  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.ADMIN);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(batchService, batchRequestMapper);
  }

  @Test
  public void testGivenCallToBatchesApiByFindingBatchesFromBatchServiceReturnListOfBatchNumbers()
    throws Exception {

    when(batchService.getBatches(ADMIN_SESSION)).thenReturn(BATCHES);

    mockMvc.perform(get("/api/core/batches").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(BATCHES_DATA_RESPONSE)
          .getJson()));

    verify(batchService).getBatches(ADMIN_SESSION);
  }

  @Test
  public void testGivenCallToBatchesApiByCreatingBatchReturnNewBatchResponse()
    throws Exception {

    batchWebRequest = BatchWebRequest.builder()
      .name(FIRST_BATCH_NAME)
      .code(FIRST_BATCH_CODE)
      .build();

    when(batchRequestMapper.toBatch(batchWebRequest)).thenReturn(FIRST_BATCH);
    when(batchService.createBatch(FIRST_BATCH)).thenReturn(FIRST_BATCH);

    mockMvc.perform(post("/api/core/batches").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        batchWebRequestJacksonTester.write(batchWebRequest)
                          .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));

    verify(batchRequestMapper).toBatch(batchWebRequest);
    verify(batchService).createBatch(FIRST_BATCH);
  }

  @Test
  public void testGivenCallToBatchesApiByGettingBatchReturnDataResponseObject()
    throws Exception {

    when(batchService.getBatchById(FIRST_BATCH_ID)).thenReturn(FIRST_BATCH);

    mockMvc.perform(get("/api/core/batches/" + FIRST_BATCH_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));

    verify(batchService).getBatchById(FIRST_BATCH_ID);
    verifyZeroInteractions(batchRequestMapper);
  }

  @Test
  public void testGivenCallToBatchesApiByUpdatingBatchReturnDataResponseObject()
    throws Exception {

    batchWebRequest = BatchWebRequest.builder()
      .id(FIRST_BATCH_ID)
      .name(FIRST_BATCH_NAME)
      .code(FIRST_BATCH_CODE)
      .build();

    when(
      batchRequestMapper.toBatch(FIRST_BATCH_ID, batchWebRequest)).thenReturn(
      FIRST_BATCH);
    when(batchService.updateBatch(FIRST_BATCH)).thenReturn(FIRST_BATCH);

    mockMvc.perform(put("/api/core/batches/" + FIRST_BATCH_ID).cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        batchWebRequestJacksonTester.write(batchWebRequest)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));

    verify(batchRequestMapper).toBatch(FIRST_BATCH_ID, batchWebRequest);
    verify(batchService).updateBatch(FIRST_BATCH);
  }

  @Test
  public void testGivenCallToBatchesApiByDeletingBatchReturnBaseResponseObject()
    throws Exception {

    mockMvc.perform(
      delete("/api/core/batches/" + FIRST_BATCH_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));

    verify(batchService).deleteBatch(FIRST_BATCH_ID);
    verifyZeroInteractions(batchRequestMapper);
  }

}
