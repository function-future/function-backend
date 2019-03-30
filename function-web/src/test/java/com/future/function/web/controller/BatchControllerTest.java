package com.future.function.web.controller;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.service.api.feature.batch.BatchService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BatchController.class)
public class BatchControllerTest {

  private static final Long FIRST_BATCH_NUMBER = 1L;

  private static final Long SECOND_BATCH_NUMBER = 2L;

  private static final Batch FIRST_BATCH =
      Batch.builder().number(FIRST_BATCH_NUMBER).deleted(false).build();

  private static final Batch SECOND_BATCH =
      Batch.builder().number(SECOND_BATCH_NUMBER).deleted(false).build();

  @Autowired private MockMvc mockMvc;

  @MockBean private BatchService batchService;

  @Before
  public void setUp() {}

  @After
  public void tearDown() {

    verifyNoMoreInteractions(batchService);
  }

  @Test
  public void
      testGivenCallToBatchesApiByFindingUndeletedBatchesFromBatchServiceReturnListOfBatchNumbers()
          throws Exception {

    given(batchService.getBatches()).willReturn(Arrays.asList(FIRST_BATCH, SECOND_BATCH));

    MockHttpServletResponse response =
        mockMvc.perform(get("/api/core/batches")).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(batchService, times(1)).getBatches();
  }

  @Test
  public void testGivenCallToBatchesApiByCreatingBatchReturnNewBatchResponse() throws Exception {

    given(batchService.createBatch()).willReturn(FIRST_BATCH);

    MockHttpServletResponse response =
        mockMvc.perform(post("/api/core/batches")).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(batchService, times(1)).createBatch();
  }

  @Test
  public void testGivenBatchNumberFromPathVariableByDeletingBatchReturnBaseResponseOK()
      throws Exception {

    MockHttpServletResponse response =
        mockMvc
            .perform(delete("/api/core/batches/" + SECOND_BATCH_NUMBER))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(batchService, times(1)).deleteBatch(SECOND_BATCH_NUMBER);
  }
}
