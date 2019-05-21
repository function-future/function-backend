package com.future.function.validation.validator.core;

import com.future.function.common.data.core.CourseData;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.BatchesMustExist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchesMustExistValidatorTest {
  
  private static final List<String> BATCH_CODES = Arrays.asList("1L", "2L");
  
  private static final List<String> BATCH_CODES_WITH_NULL = Arrays.asList(
    "2L", null);
  
  @Mock
  private CourseData courseData;
  
  @Mock
  private BatchRepository batchRepository;
  
  @Mock
  private BatchesMustExist annotation;
  
  @InjectMocks
  private BatchesMustExistValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, courseData, batchRepository);
  }
  
  @Test
  public void testGivenListOfBatchNumberOfExistingBatchByCheckingExistingBatchInDatabaseReturnTrue() {
    
    when(courseData.getBatchCodes()).thenReturn(BATCH_CODES);
    when(batchRepository.findByCode(BATCH_CODES.get(0))).thenReturn(
      Optional.of(Batch.builder().code(BATCH_CODES.get(0)).build()));
    when(batchRepository.findByCode(BATCH_CODES.get(1))).thenReturn(
      Optional.of(Batch.builder().code(BATCH_CODES.get(1)).build()));
    
    assertThat(validator.isValid(courseData, null)).isTrue();
    
    verify(courseData).getBatchCodes();
    verify(batchRepository).findByCode(BATCH_CODES.get(0));
    verify(batchRepository).findByCode(BATCH_CODES.get(1));
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenListOfBatchNumberOfNonExistingBatchesByCheckingExistingBatchInDatabaseReturnFalse() {
    
    when(courseData.getBatchCodes()).thenReturn(BATCH_CODES);
    when(batchRepository.findByCode(BATCH_CODES.get(0))).thenReturn(
      Optional.of(Batch.builder().code(BATCH_CODES.get(0)).build()));
    when(batchRepository.findByCode(BATCH_CODES.get(1))).thenReturn(
      Optional.empty());
    
    assertThat(validator.isValid(courseData, null)).isFalse();
    
    verify(courseData).getBatchCodes();
    verify(batchRepository).findByCode(BATCH_CODES.get(0));
    verify(batchRepository).findByCode(BATCH_CODES.get(1));
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenListOfBatchNumberWithNullByCheckingExistingBatchInDatabaseReturnFalse() {
    
    when(courseData.getBatchCodes()).thenReturn(BATCH_CODES_WITH_NULL);
    
    assertThat(validator.isValid(courseData, null)).isFalse();
    
    verify(courseData).getBatchCodes();
    verifyZeroInteractions(batchRepository);
    verifyZeroInteractions(annotation);
  }
  
}
