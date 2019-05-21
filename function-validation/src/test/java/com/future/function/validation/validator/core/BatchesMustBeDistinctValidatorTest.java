package com.future.function.validation.validator.core;

import com.future.function.common.data.core.CourseData;
import com.future.function.validation.annotation.core.BatchesMustBeDistinct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchesMustBeDistinctValidatorTest {
  
  private static final List<String> VALID_LIST = Arrays.asList("1L", "2L");
  
  private static final List<String> INVALID_LIST = Arrays.asList("1L", "2L",
                                                               "2L");
  
  private static final List<String> INVALID_LIST_WITH_NULL = Arrays.asList(
    "1L", null);
  
  @Mock
  private CourseData courseData;
  
  @Mock
  private BatchesMustBeDistinct annotation;
  
  @InjectMocks
  private BatchesMustBeDistinctValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, courseData);
  }
  
  @Test
  public void testGivenValidListByCheckingNoDuplicateInListReturnTrue() {
    
    when(courseData.getBatchCodes()).thenReturn(VALID_LIST);
    
    assertThat(validator.isValid(courseData, null)).isTrue();
    
    verify(courseData).getBatchCodes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenInvalidListByCheckingNoDuplicateInListReturnFalse() {
    
    when(courseData.getBatchCodes()).thenReturn(INVALID_LIST);
    
    assertThat(validator.isValid(courseData, null)).isFalse();
    
    verify(courseData).getBatchCodes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenInvalidListWithNullByCheckingNoDuplicateInListReturnFalse() {
    
    when(courseData.getBatchCodes()).thenReturn(INVALID_LIST_WITH_NULL);
    
    assertThat(validator.isValid(courseData, null)).isFalse();
    
    verify(courseData).getBatchCodes();
    verifyZeroInteractions(annotation);
  }
  
}
