package com.future.function.validation.validator.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.BatchMustExist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchMustExistValidatorTest {
  
  private static final String BATCH_CODE = "1L";
  
  @Mock
  private BatchRepository batchRepository;
  
  @Mock
  private BatchMustExist annotation;
  
  @InjectMocks
  private BatchMustExistValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, batchRepository);
  }
  
  @Test
  public void testGivenExistingBatchCodeOfExistingBatchByCheckingExistingBatchInDatabaseReturnTrue() {
    
    when(batchRepository.findByCodeAndDeletedFalse(BATCH_CODE)).thenReturn(Optional.of(
      Batch.builder()
        .code(BATCH_CODE)
        .build()));
    
    assertThat(validator.isValid(BATCH_CODE, null)).isTrue();
    
    verify(batchRepository).findByCodeAndDeletedFalse(BATCH_CODE);
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNonExistingBatchCodeByCheckingExistingBatchInDatabaseReturnFalse() {
    
    when(batchRepository.findByCodeAndDeletedFalse(BATCH_CODE)).thenReturn(Optional.empty());
    
    assertThat(validator.isValid(BATCH_CODE, null)).isFalse();
    
    verify(batchRepository).findByCodeAndDeletedFalse(BATCH_CODE);
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNullBatchCodeIndicatingCreateSharedCourseFromMasterDataByCheckingExistingBatchInDatabaseReturnTrue() {
    
    assertThat(validator.isValid(null, null)).isTrue();
    
    verifyZeroInteractions(batchRepository, annotation);
  }
  
  @Test
  public void testGivenEmptyBatchCodeIndicatingCreateSharedCourseFromMasterDataByCheckingExistingBatchInDatabaseReturnTrue() {
    
    assertThat(validator.isValid("", null)).isTrue();
    
    verifyZeroInteractions(batchRepository, annotation);
  }
  
}
