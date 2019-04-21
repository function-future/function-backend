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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchesMustExistValidatorTest {
  
  private static final List<Long> BATCH_NUMBERS = Arrays.asList(1L, 2L);
  
  private static final List<Long> BATCH_NUMBERS_WITH_NULL = Arrays.asList(
    2L, null);
  
  private static final Iterable<Batch> BATCH_ITERABLE = Arrays.asList(
    new Batch(1L), new Batch(2L));
  
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
    
    when(courseData.getBatchNumbers()).thenReturn(BATCH_NUMBERS);
    when(batchRepository.findAll(BATCH_NUMBERS)).thenReturn(BATCH_ITERABLE);
    
    assertThat(validator.isValid(courseData, null)).isTrue();
    
    verify(courseData).getBatchNumbers();
    verify(batchRepository).findAll(BATCH_NUMBERS);
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenListOfBatchNumberOfNonExistingBatchesByCheckingExistingBatchInDatabaseReturnFalse() {
    
    when(courseData.getBatchNumbers()).thenReturn(BATCH_NUMBERS);
    when(batchRepository.findAll(BATCH_NUMBERS)).thenReturn(
      Collections.singletonList(new Batch(1L)));
    
    assertThat(validator.isValid(courseData, null)).isFalse();
    
    verify(courseData).getBatchNumbers();
    verify(batchRepository).findAll(BATCH_NUMBERS);
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenListOfBatchNumberWithNullByCheckingExistingBatchInDatabaseReturnFalse() {
    
    when(courseData.getBatchNumbers()).thenReturn(BATCH_NUMBERS_WITH_NULL);
    
    assertThat(validator.isValid(courseData, null)).isFalse();
    
    verify(courseData).getBatchNumbers();
    verifyZeroInteractions(batchRepository);
    verifyZeroInteractions(annotation);
  }
  
}
