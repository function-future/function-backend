package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.repository.feature.core.SequenceGenerator;
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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
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
  
  @Mock
  private SequenceGenerator sequenceGenerator;
  
  @InjectMocks
  private BatchServiceImpl batchService;
  
  @Before
  public void setUp() {
    
    batch = Batch.builder()
      .number(FIRST_BATCH_NUMBER)
      .build();
  
  }
  
  @After
  public void tearDown() {
  
    verifyNoMoreInteractions(batchRepository, sequenceGenerator);
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchByCreatingBatchReturnNewBatchObject() {
  
    when(sequenceGenerator.increment(Batch.SEQUENCE_NAME)).thenReturn(
      FIRST_BATCH_NUMBER);
    when(batchRepository.save(new Batch(FIRST_BATCH_NUMBER))).thenReturn(batch);
    when(
      batchRepository.findFirstByNumberIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(batch));
    
    Batch createdBatch = batchService.createBatch();
  
    assertThat(createdBatch).isNotNull();
    assertThat(createdBatch).isEqualTo(batch);
  
    verify(sequenceGenerator).increment(Batch.SEQUENCE_NAME);
    verify(batchRepository).save(new Batch(FIRST_BATCH_NUMBER));
    verify(batchRepository).findFirstByNumberIsNotNullOrderByUpdatedAtDesc();
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchButSomehowFailedInDatabaseProcessingByCreatingBatchReturnNotFoundException() {
  
    when(sequenceGenerator.increment(Batch.SEQUENCE_NAME)).thenReturn(
      FIRST_BATCH_NUMBER);
    when(batchRepository.save(new Batch(FIRST_BATCH_NUMBER))).thenReturn(batch);
    when(
      batchRepository.findFirstByNumberIsNotNullOrderByUpdatedAtDesc()).thenThrow(
      new NotFoundException("Saved Batch Not Found"));
  
    catchException(() -> batchService.createBatch());
  
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Saved Batch Not Found");
    
    verify(sequenceGenerator).increment(Batch.SEQUENCE_NAME);
    verify(batchRepository).save(new Batch(FIRST_BATCH_NUMBER));
    verify(batchRepository).findFirstByNumberIsNotNullOrderByUpdatedAtDesc();
  }
  
  @Test
  public void testGivenExistingBatchInDatabaseByFindingBatchByNumberReturnBatchObject() {
  
    when(batchRepository.findOne(FIRST_BATCH_NUMBER)).thenReturn(batch);
    
    Batch foundBatch = batchService.getBatch(FIRST_BATCH_NUMBER);
    
    assertThat(foundBatch).isNotNull();
    assertThat(foundBatch).isEqualTo(batch);
  
    verify(batchRepository).findOne(FIRST_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenNonExistingBatchInDatabaseByFindingBatchByNumberReturnNull() {
  
    when(batchRepository.findOne(FIRST_BATCH_NUMBER)).thenReturn(null);
  
    catchException(() -> batchService.getBatch(FIRST_BATCH_NUMBER));
  
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get Batch Not Found");
    
    verify(batchRepository).findOne(FIRST_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenExistingBatchesInDatabaseByFindingBatchesReturnListOfBatch() {
    
    Batch secondBatch = Batch.builder()
      .number(SECOND_BATCH_NUMBER)
      .build();
    
    List<Batch> batchList = Arrays.asList(batch, secondBatch);
  
    when(batchRepository.findAll()).thenReturn(batchList);
    
    List<Batch> foundBatchList = batchService.getBatches();
    
    assertThat(foundBatchList).isNotEmpty();
    assertThat(foundBatchList).isEqualTo(batchList);
  
    verify(batchRepository).findAll();
  }
  
  @Test
  public void testGivenNoExistingBatchInDatabaseByFindingBatchesReturnEmptyList() {
    
    List<Batch> batchList = Collections.emptyList();
  
    when(batchRepository.findAll()).thenReturn(batchList);
    
    List<Batch> foundBatchList = batchService.getBatches();
    
    assertThat(foundBatchList).isEmpty();
  
    verify(batchRepository).findAll();
  }
  
}
