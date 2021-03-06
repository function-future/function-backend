package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.session.model.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

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

  private static final String FIRST_BATCH_NUMBER = "1";

  private static final String SECOND_BATCH_NUMBER = "2";

  private static final String ID_1 = "id-1";

  private static final String NAME_1 = "name-1";

  private Batch batch;

  @Mock
  private BatchRepository batchRepository;

  @InjectMocks
  private BatchServiceImpl batchService;

  @Before
  public void setUp() {

    batch = Batch.builder()
      .id(ID_1)
      .name(NAME_1)
      .code(FIRST_BATCH_NUMBER)
      .build();
    batch.setCreatedAt(5L);
    batch.setUpdatedAt(10L);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(batchRepository);
  }

  @Test
  public void testGivenMethodCallToCreateBatchByCreatingBatchReturnNewBatchObject() {

    when(batchRepository.save(batch)).thenReturn(batch);
    when(
      batchRepository.findFirstByDeletedFalseOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(batch));

    Batch createdBatch = batchService.createBatch(batch);

    assertThat(createdBatch).isNotNull();
    assertThat(createdBatch).isEqualTo(batch);

    verify(batchRepository).save(batch);
    verify(batchRepository).findFirstByDeletedFalseOrderByUpdatedAtDesc();
  }

  @Test
  public void testGivenMethodCallToCreateBatchButSomehowFailedInDatabaseProcessingByCreatingBatchReturnNotFoundException() {

    when(batchRepository.save(batch)).thenReturn(batch);
    when(
      batchRepository.findFirstByDeletedFalseOrderByUpdatedAtDesc()).thenThrow(
      new NotFoundException("Saved Batch Not Found"));

    catchException(() -> batchService.createBatch(batch));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Saved Batch Not Found");

    verify(batchRepository).save(batch);
    verify(batchRepository).findFirstByDeletedFalseOrderByUpdatedAtDesc();
  }

  @Test
  public void testGivenExistingBatchInDatabaseByFindingBatchByCodeReturnBatchObject() {

    when(
      batchRepository.findByCodeAndDeletedFalse(FIRST_BATCH_NUMBER)).thenReturn(
      Optional.of(batch));

    Batch foundBatch = batchService.getBatchByCode(FIRST_BATCH_NUMBER);

    assertThat(foundBatch).isNotNull();
    assertThat(foundBatch).isEqualTo(batch);

    verify(batchRepository).findByCodeAndDeletedFalse(FIRST_BATCH_NUMBER);
  }

  @Test
  public void testGivenNonExistingBatchInDatabaseByFindingBatchByCodeReturnNull() {

    when(
      batchRepository.findByCodeAndDeletedFalse(FIRST_BATCH_NUMBER)).thenReturn(
      Optional.empty());

    catchException(() -> batchService.getBatchByCode(FIRST_BATCH_NUMBER));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get Batch Not Found");

    verify(batchRepository).findByCodeAndDeletedFalse(FIRST_BATCH_NUMBER);
  }

  @Test
  public void testGivenExistingBatchInDatabaseByFindingBatchByIdReturnBatchObject() {

    when(batchRepository.findOne(ID_1)).thenReturn(batch);

    Batch foundBatch = batchService.getBatchById(ID_1);

    assertThat(foundBatch).isNotNull();
    assertThat(foundBatch).isEqualTo(batch);

    verify(batchRepository).findOne(ID_1);
  }

  @Test
  public void testGivenExistingDeletedBatchInDatabaseByFindingBatchByIdReturnNotFoundException() {

    batch.setDeleted(true);
    when(batchRepository.findOne(ID_1)).thenReturn(batch);

    catchException(() -> batchService.getBatchById(ID_1));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get Batch Not Found");

    verify(batchRepository).findOne(ID_1);
  }

  @Test
  public void testGivenNonExistingBatchInDatabaseByFindingBatchByIdReturnNull() {

    when(batchRepository.findOne(ID_1)).thenReturn(null);

    catchException(() -> batchService.getBatchById(ID_1));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get Batch Not Found");

    verify(batchRepository).findOne(ID_1);
  }

  @Test
  public void testGivenExistingBatchesInDatabaseByFindingBatchesReturnListOfBatch() {

    Batch secondBatch = Batch.builder()
      .code(SECOND_BATCH_NUMBER)
      .build();

    List<Batch> batches = Arrays.asList(secondBatch, batch);

    when(batchRepository.findAllByDeletedFalse()).thenReturn(batches);

    Session sessionJudge = Session.builder()
      .role(Role.JUDGE)
      .build();
    List<Batch> foundBatchListJudge = batchService.getBatches(sessionJudge);

    assertThat(foundBatchListJudge).isNotEmpty();
    assertThat(foundBatchListJudge).isEqualTo(batches);

    verify(batchRepository).findAllByDeletedFalse();

    when(batchRepository.findAllByIdAndDeletedFalse(ID_1)).thenReturn(batches);

    Session sessionStudent = Session.builder()
      .batchId(ID_1)
      .role(Role.STUDENT)
      .build();
    List<Batch> foundBatchListStudent = batchService.getBatches(sessionStudent);

    assertThat(foundBatchListStudent).isNotEmpty();
    assertThat(foundBatchListStudent).isEqualTo(batches);

    verify(batchRepository).findAllByIdAndDeletedFalse(ID_1);
  }

  @Test
  public void testGivenNoExistingBatchInDatabaseByFindingBatchesReturnEmptyList() {

    List<Batch> batches = Collections.emptyList();

    when(batchRepository.findAllByDeletedFalse()).thenReturn(batches);

    Session sessionJudge = Session.builder()
      .role(Role.JUDGE)
      .build();
    List<Batch> foundBatchListJudge = batchService.getBatches(sessionJudge);

    assertThat(foundBatchListJudge).isEmpty();

    verify(batchRepository).findAllByDeletedFalse();

    when(batchRepository.findAllByIdAndDeletedFalse(ID_1)).thenReturn(batches);

    Session sessionStudent = Session.builder()
      .batchId(ID_1)
      .role(Role.STUDENT)
      .build();
    List<Batch> foundBatchListStudent = batchService.getBatches(sessionStudent);

    assertThat(foundBatchListStudent).isEmpty();

    verify(batchRepository).findAllByIdAndDeletedFalse(ID_1);
  }

  @Test
  public void testGivenBatchIdByDeletingBatchReturnSuccessfulDeletion() {

    when(batchRepository.findOne(ID_1)).thenReturn(batch);

    batchService.deleteBatch(ID_1);

    assertThat(batch.isDeleted()).isTrue();

    verify(batchRepository).findOne(ID_1);
    verify(batchRepository).save(batch);
  }

  @Test
  public void testGivenBatchIdAndDeletedBatchByDeletingBatchReturnSuccessfulButNoDeletion() {

    batch.setDeleted(true);
    when(batchRepository.findOne(ID_1)).thenReturn(batch);

    batchService.deleteBatch(ID_1);

    assertThat(batch.isDeleted()).isTrue();

    verify(batchRepository).findOne(ID_1);
  }

  @Test
  public void testGivenMethodCallToUpdateBatchByUpdatingBatchReturnUpdatedBatchObject() {

    when(batchRepository.findOne(ID_1)).thenReturn(batch);
    when(batchRepository.save(batch)).thenReturn(batch);
    when(
      batchRepository.findFirstByDeletedFalseOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(batch));

    Batch updatedBatch = batchService.updateBatch(batch);

    assertThat(updatedBatch).isNotNull();
    assertThat(updatedBatch).isEqualTo(batch);

    verify(batchRepository).findOne(ID_1);
    verify(batchRepository).save(batch);
    verify(batchRepository).findFirstByDeletedFalseOrderByUpdatedAtDesc();
  }

  @Test
  public void testGivenMethodCallToUpdateDeletedBatchByUpdatingBatchReturnRequestBatchObject() {

    Batch batch = new Batch();
    BeanUtils.copyProperties(this.batch, batch);
    batch.setDeleted(true);
    when(batchRepository.findOne(ID_1)).thenReturn(batch);

    Batch updatedBatch = batchService.updateBatch(this.batch);

    assertThat(updatedBatch).isNotNull();
    assertThat(updatedBatch).isEqualTo(this.batch);

    verify(batchRepository).findOne(ID_1);
  }

  @Test
  public void testGivenMethodCallToUpdateNonExistingBatchByUpdatingBatchReturnRequestBatchObject() {

    when(batchRepository.findOne(ID_1)).thenReturn(null);

    Batch returnedButNotUpdatedBatch = batchService.updateBatch(batch);

    assertThat(returnedButNotUpdatedBatch).isNotNull();
    assertThat(returnedButNotUpdatedBatch).isEqualTo(batch);

    verify(batchRepository).findOne(ID_1);
  }

}
