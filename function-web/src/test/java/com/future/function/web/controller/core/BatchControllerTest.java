package com.future.function.web.controller.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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
  
  private static final Long FIRST_BATCH_NUMBER = 1L;
  
  private static final Long SECOND_BATCH_NUMBER = 2L;
  
  private static final Batch FIRST_BATCH = Batch.builder()
    .code(FIRST_BATCH_NUMBER)
    .build();
  
  private static final Batch SECOND_BATCH = Batch.builder()
    .code(SECOND_BATCH_NUMBER)
    .build();
  
  private static final DataResponse<List<Long>> BATCHES_DATA_RESPONSE =
    BatchResponseMapper.toBatchesDataResponse(
      Arrays.asList(FIRST_BATCH, SECOND_BATCH));
  
  private static final DataResponse<BatchWebResponse>
    FIRST_BATCH_DATA_RESPONSE = BatchResponseMapper.toBatchDataResponse(
    FIRST_BATCH);
  
  private JacksonTester<DataResponse<List<Long>>> listDataResponseJacksonTester;
  
  private JacksonTester<DataResponse<BatchWebResponse>>
    dataResponseJacksonTester;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private BatchService batchService;
  
  @Before
  public void setUp() {
  
    JacksonTester.initFields(this, new ObjectMapper());
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(batchService);
  }
  
  @Test
  public void testGivenCallToBatchesApiByFindingBatchesFromBatchServiceReturnListOfBatchNumbers()
    throws Exception {
  
    List<Batch> batches = Arrays.asList(FIRST_BATCH, SECOND_BATCH);
    given(batchService.getBatches()).willReturn(batches);
  
    mockMvc.perform(get("/api/core/batches"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        listDataResponseJacksonTester.write(BATCHES_DATA_RESPONSE)
          .getJson()));
    
    verify(batchService).getBatches();
  }
  
  @Test
  public void testGivenCallToBatchesApiByCreatingBatchReturnNewBatchResponse()
    throws Exception {
    
    given(batchService.createBatch()).willReturn(FIRST_BATCH);
  
    mockMvc.perform(post("/api/core/batches"))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(FIRST_BATCH_DATA_RESPONSE)
          .getJson()));
    
    verify(batchService).createBatch();
  }
  
}
