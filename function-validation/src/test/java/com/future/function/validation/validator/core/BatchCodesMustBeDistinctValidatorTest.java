package com.future.function.validation.validator.core;

import com.future.function.common.data.core.SharedCourseData;
import com.future.function.validation.annotation.core.BatchCodesMustBeDistinct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BatchCodesMustBeDistinctValidatorTest {
  
  private static final String TARGET_BATCH = "target-batch";
  
  private static final String ORIGIN_BATCH = "origin-batch";
  
  @Mock
  private BatchCodesMustBeDistinct annotation;
  
  @Mock
  private SharedCourseData sharedCourseData;
  
  @InjectMocks
  private BatchCodesMustBeDistinctValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, sharedCourseData);
  }
  
  @Test
  public void testGivenDistinctOriginBatchAndTargetBatchByValidatingBatchCodesMustBeDistinctReturnTrue() {
    
    when(sharedCourseData.getOriginBatch()).thenReturn(ORIGIN_BATCH);
    when(sharedCourseData.getTargetBatch()).thenReturn(TARGET_BATCH);
    
    assertThat(validator.isValid(sharedCourseData, null)).isTrue();
    
    verify(sharedCourseData).getOriginBatch();
    verify(sharedCourseData).getTargetBatch();
    
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNonDistinctOriginBatchAndTargetBatchByValidatingBatchCodesMustBeDistinctReturnFalse() {
  
    when(sharedCourseData.getOriginBatch()).thenReturn(TARGET_BATCH);
    when(sharedCourseData.getTargetBatch()).thenReturn(TARGET_BATCH);
  
    assertThat(validator.isValid(sharedCourseData, null)).isFalse();
  
    verify(sharedCourseData).getOriginBatch();
    verify(sharedCourseData).getTargetBatch();
  
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNullValueForOriginBatchByValidatingBatchCodesMustBeDistinctReturnTrue() {
  
    when(sharedCourseData.getOriginBatch()).thenReturn(null);
    when(sharedCourseData.getTargetBatch()).thenReturn(TARGET_BATCH);
  
    assertThat(validator.isValid(sharedCourseData, null)).isTrue();
  
    verify(sharedCourseData).getOriginBatch();
    verify(sharedCourseData).getTargetBatch();
  
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNullValueForTargetBatchByValidatingBatchCodesMustBeDistinctReturnFalse() {
  
    when(sharedCourseData.getOriginBatch()).thenReturn(ORIGIN_BATCH);
    when(sharedCourseData.getTargetBatch()).thenReturn(null);
  
    assertThat(validator.isValid(sharedCourseData, null)).isFalse();
  
    verify(sharedCourseData).getOriginBatch();
    verify(sharedCourseData).getTargetBatch();
  
    verifyZeroInteractions(annotation);
  }
  
}
