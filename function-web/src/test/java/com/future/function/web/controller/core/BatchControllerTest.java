package com.future.function.web.controller.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.mapper.request.core.BatchRequestMapper;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.request.core.BatchWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BatchController.class)
public class BatchControllerTest {
  
  private static final Long FIRST_BATCH_CODE = 1L;
  
  private static final String FIRST_BATCH_NAME = "name-1";
  
  private static final Long SECOND_BATCH_CODE = 2L;
  
  private static final String SECOND_BATCH_NAME = "name-2";
  
  private static final Batch FIRST_BATCH = Batch.builder()
    .name(FIRST_BATCH_NAME)
    .code(FIRST_BATCH_CODE)
    .build();
  
  private static final Batch SECOND_BATCH = Batch.builder()
    .name(SECOND_BATCH_NAME)
    .code(SECOND_BATCH_CODE)
    .build();
  
  private static final BatchWebRequest BATCH_WEB_REQUEST =
    BatchWebRequest.builder()
      .name(FIRST_BATCH_NAME)
      .code(FIRST_BATCH_CODE)
      .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  private static final Page<Batch> BATCH_PAGE = new PageImpl<>(
    Arrays.asList(FIRST_BATCH, SECOND_BATCH), PAGEABLE, 2);
  
  private static final PagingResponse<BatchWebResponse> BATCHES_DATA_RESPONSE =
    BatchResponseMapper.toBatchesPagingResponse(BATCH_PAGE);
  
  private static final DataResponse<BatchWebResponse>
    FIRST_BATCH_DATA_RESPONSE = BatchResponseMapper.toBatchDataResponse(
    FIRST_BATCH);
  
  private JacksonTester<BatchWebRequest> batchWebRequestJacksonTester;
  
  private JacksonTester<PagingResponse<BatchWebResponse>>
    pagingResponseJacksonTester;
  
  private JacksonTester<DataResponse<BatchWebResponse>>
    dataResponseJacksonTester;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private BatchService batchService;
  
  @MockBean
  private BatchRequestMapper batchRequestMapper;
  
  @Before
  public void setUp() {
    
    JacksonTester.initFields(this, new ObjectMapper());
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(batchService, batchRequestMapper);
  }
  
  @Test
  public void testGivenCallToBatchesApiByFindingBatchesFromBatchServiceReturnListOfBatchNumbers()
    throws Exception {
    
    given(batchService.getBatches(PAGEABLE)).willReturn(BATCH_PAGE);
    
    mockMvc.perform(get("/api/core/batches"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(BATCHES_DATA_RESPONSE)
          .getJson()));
    
    verify(batchService).getBatches(PAGEABLE);
  }
  
  @Test
  public void testGivenCallToBatchesApiByCreatingBatchReturnNewBatchResponse()
    throws Exception {
    
    given(batchRequestMapper.toBatch(BATCH_WEB_REQUEST)).willReturn(
      FIRST_BATCH);
    given(batchService.createBatch(FIRST_BATCH)).willReturn(FIRST_BATCH);
    
    mockMvc.perform(post("/api/core/batches").contentType(
      MediaType.APPLICATION_JSON)
                      .content(
                        batchWebRequestJacksonTester.write(BATCH_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(FIRST_BATCH_DATA_RESPONSE)
          .getJson()));
    
    verify(batchRequestMapper).toBatch(BATCH_WEB_REQUEST);
    verify(batchService).createBatch(FIRST_BATCH);
  }
  
}
