package com.future.function.service.impl.feature.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.repository.feature.batch.BatchRepository;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {

  private static final Long NUMBER = 1L;

  private Batch batch;

  @Mock
  private BatchRepository batchRepository;

  @InjectMocks
  private BatchServiceImpl batchService;

  @Before
  public void setUp() {

    batch = Batch.builder()
        .number(NUMBER)
        .build();

    when(batchRepository.findByNumber(NUMBER)).thenReturn(Optional.of(batch));
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(batchRepository);
  }

  @Test
  public void testGivenExistingBatchInDatabaseByFindingBatchByNumberReturnBatchObject() {

    Batch foundBatch = batchService.findByNumber(NUMBER);

    assertThat(foundBatch).isNotNull();
    assertThat(foundBatch).isEqualTo(batch);

    verify(batchRepository, times(1)).findByNumber(NUMBER);
  }

  @Test
  public void testGivenNonExistingBatchInDatabaseByFindingBatchByNumberReturnNull() {

    try {
      batchService.findByNumber(NUMBER);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(RuntimeException.class);
      assertThat(e.getMessage()).isEqualTo("Not Found");
    }

    verify(batchRepository, times(1)).findByNumber(NUMBER);
  }

}
