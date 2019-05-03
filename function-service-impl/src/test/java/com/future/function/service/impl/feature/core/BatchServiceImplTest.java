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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {
  
  private static final String FIRST_BATCH_NUMBER = "1";
  
  private static final String SECOND_BATCH_NUMBER = "2";
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  private Batch batch;
  
  @Mock
  private BatchRepository batchRepository;
  
  @InjectMocks
  private BatchServiceImpl batchService;
  
  @Before
  public void setUp() {
    
    batch = Batch.builder()
      .id("id-1")
      .name("name-1")
      .code(FIRST_BATCH_NUMBER)
      .build();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(batchRepository);
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchByCreatingBatchReturnNewBatchObject() {
    
    when(batchRepository.save(
      new Batch("id-1", "one", FIRST_BATCH_NUMBER))).thenReturn(batch);
    when(
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(batch));
    
    Batch createdBatch = batchService.createBatch(
      new Batch("id-1", "one", FIRST_BATCH_NUMBER));
    
    assertThat(createdBatch).isNotNull();
    assertThat(createdBatch).isEqualTo(batch);
    
    verify(batchRepository).save(new Batch("id-1", "one", FIRST_BATCH_NUMBER));
    verify(batchRepository).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  }
  
  @Test
  public void testGivenMethodCallToCreateBatchButSomehowFailedInDatabaseProcessingByCreatingBatchReturnNotFoundException() {
    
    when(batchRepository.save(
      new Batch("id-1", "one", FIRST_BATCH_NUMBER))).thenReturn(batch);
    when(
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenThrow(
      new NotFoundException("Saved Batch Not Found"));
    
    catchException(() -> batchService.createBatch(
      new Batch("id-1", "one", FIRST_BATCH_NUMBER)));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Saved Batch Not Found");
    
    verify(batchRepository).save(new Batch("id-1", "one", FIRST_BATCH_NUMBER));
    verify(batchRepository).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  }
  
  @Test
  public void testGivenExistingBatchInDatabaseByFindingBatchByNumberReturnBatchObject() {
    
    when(batchRepository.findByCode(FIRST_BATCH_NUMBER)).thenReturn(
      Optional.of(batch));
    
    Batch foundBatch = batchService.getBatch(FIRST_BATCH_NUMBER);
    
    assertThat(foundBatch).isNotNull();
    assertThat(foundBatch).isEqualTo(batch);
    
    verify(batchRepository).findByCode(FIRST_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenNonExistingBatchInDatabaseByFindingBatchByNumberReturnNull() {
    
    when(batchRepository.findByCode(FIRST_BATCH_NUMBER)).thenReturn(
      Optional.empty());
    
    catchException(() -> batchService.getBatch(FIRST_BATCH_NUMBER));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get Batch Not Found");
    
    verify(batchRepository).findByCode(FIRST_BATCH_NUMBER);
  }
  
  @Test
  public void testGivenExistingBatchesInDatabaseByFindingBatchesReturnListOfBatch() {
    
    Batch secondBatch = Batch.builder()
      .code(SECOND_BATCH_NUMBER)
      .build();
    
    Page<Batch> batchPage = new PageImpl<>(
      Arrays.asList(secondBatch, batch), PAGEABLE, 2);
    
    when(batchRepository.findAllByIdIsNotNullOrderByUpdatedAtDesc(
      PAGEABLE)).thenReturn(batchPage);
    
    Page<Batch> foundBatchPage = batchService.getBatches(PAGEABLE);
    
    assertThat(foundBatchPage.getContent()).isNotEmpty();
    assertThat(foundBatchPage).isEqualTo(batchPage);
    
    verify(batchRepository).findAllByIdIsNotNullOrderByUpdatedAtDesc(PAGEABLE);
  }
  
  @Test
  public void testGivenNoExistingBatchInDatabaseByFindingBatchesReturnEmptyList() {
    
    Page<Batch> batchPage = new PageImpl<>(Collections.emptyList(), PAGEABLE,
                                           0
    );
    
    when(batchRepository.findAllByIdIsNotNullOrderByUpdatedAtDesc(
      PAGEABLE)).thenReturn(batchPage);
    
    Page<Batch> foundBatchPage = batchService.getBatches(PAGEABLE);
    
    assertThat(foundBatchPage.getContent()).isEmpty();
    
    verify(batchRepository).findAllByIdIsNotNullOrderByUpdatedAtDesc(PAGEABLE);
  }
  
}
