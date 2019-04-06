package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {
  
  private static final Long FIRST_BATCH_NUMBER = 1L;
  
  private static final Long SECOND_BATCH_NUMBER = 2L;
  
  private Batch batch;
  
  @Mock
  private BatchRepository batchRepository;
  
  @InjectMocks
  private BatchServiceImpl batchService;
  
  @Before
  public void setUp() {
    
    batch = Batch.builder()
      .number(FIRST_BATCH_NUMBER)
      .deleted(false)
      .build();
    
    when(batchRepository.findByNumberAndDeletedIsFalse(
      FIRST_BATCH_NUMBER)).thenReturn(Optional.of(batch));
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(batchRepository);
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchWhileNoBatchExistedBeforeByCreatingBatchReturnNewBatchObject() {
    
    when(
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.empty());
    when(batchRepository.save(batch)).thenReturn(batch);
    
    Batch createdBatch = batchService.createBatch();
    
    assertThat(createdBatch).isEqualTo(batch);
    
    verify(
      batchRepository, times(1)).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
    verify(batchRepository, times(1)).save(batch);
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchWhileAUndeletedBatchExistedBeforeByCreatingBatchReturnNewBatchObject() {
    
    when(
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(batch));
    
    Batch secondBatch = Batch.builder()
      .number(SECOND_BATCH_NUMBER)
      .deleted(false)
      .build();
    
    when(batchRepository.save(secondBatch)).thenReturn(secondBatch);
    
    Batch createdBatch = batchService.createBatch();
    
    assertThat(createdBatch).isEqualTo(secondBatch);
    
    verify(
      batchRepository, times(1)).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
    verify(batchRepository, times(1)).save(secondBatch);
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchWhileADeletedBatchExistedBeforeByCreatingBatchReturnNewBatchObject() {
    
    batch.setDeleted(true);
    
    when(
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(batch));
    
    batch.setNumber(SECOND_BATCH_NUMBER);
    
    when(batchRepository.save(batch)).thenReturn(batch);
    
    Batch createdBatch = batchService.createBatch();
    
    assertThat(createdBatch).isEqualTo(batch);
    
    verify(
      batchRepository, times(1)).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
    verify(batchRepository, times(1)).save(batch);
  }
  
  @Test
  public void testGivenExistingBatchInDatabaseByFindingBatchByNumberReturnBatchObject() {
    
    Batch foundBatch = batchService.getBatch(FIRST_BATCH_NUMBER);
    
    assertThat(foundBatch).isNotNull();
    assertThat(foundBatch).isEqualTo(batch);
    
    verify(batchRepository, times(1)).findByNumberAndDeletedIsFalse(
      FIRST_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenExistingBatchesInDatabaseByFindingBatchesReturnListOfBatch() {
    
    Batch secondBatch = Batch.builder()
      .number(SECOND_BATCH_NUMBER)
      .deleted(false)
      .build();
    
    List<Batch> batchList = Arrays.asList(batch, secondBatch);
    
    when(batchRepository.findAllByDeletedIsFalse()).thenReturn(batchList);
    
    List<Batch> foundBatchList = batchService.getBatches();
    
    assertThat(foundBatchList).isNotEmpty();
    assertThat(foundBatchList).isEqualTo(batchList);
    
    verify(batchRepository, times(1)).findAllByDeletedIsFalse();
  }
  
  @Test
  public void testGivenNoExistingBatchInDatabaseByFindingBatchesReturnEmptyList() {
    
    List<Batch> batchList = Collections.emptyList();
    
    when(batchRepository.findAllByDeletedIsFalse()).thenReturn(batchList);
    
    List<Batch> foundBatchList = batchService.getBatches();
    
    assertThat(foundBatchList).isEmpty();
    
    verify(batchRepository, times(1)).findAllByDeletedIsFalse();
  }
  
  @Test
  public void testGivenNonExistingBatchInDatabaseByFindingBatchByNumberReturnNull() {
    
    try {
      batchService.getBatch(FIRST_BATCH_NUMBER);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Get Batch Not Found");
    }
    
    verify(batchRepository, times(1)).findByNumberAndDeletedIsFalse(
      FIRST_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenNonExistingBatchInDatabaseByDeletingBatchByNumberReturnRuntimeException() {
    
    when(batchRepository.findByNumberAndDeletedIsFalse(
      SECOND_BATCH_NUMBER)).thenReturn(Optional.empty());
    
    try {
      batchService.deleteBatch(SECOND_BATCH_NUMBER);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Delete Batch Not Found");
    }
    
    verify(batchRepository, times(1)).findByNumberAndDeletedIsFalse(
      SECOND_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenExistingBatchInDatabaseByDeletingBatchByNumberReturnSuccessfulDeletion() {
    
    when(batchRepository.findByNumberAndDeletedIsFalse(
      FIRST_BATCH_NUMBER)).thenReturn(Optional.of(batch));
    
    batch.setDeleted(true);
    when(batchRepository.save(batch)).thenReturn(batch);
    
    batchService.deleteBatch(FIRST_BATCH_NUMBER);
    
    verify(batchRepository, times(1)).findByNumberAndDeletedIsFalse(
      FIRST_BATCH_NUMBER);
    verify(batchRepository, times(1)).save(batch);
  }
  
}
