package com.future.function.validation.validator.core;

import com.future.function.common.data.core.FileData;
import com.future.function.validation.annotation.core.TypeAndBytesMustBeValid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TypeAndBytesMustBeValidValidatorTest {
  
  @Mock
  private TypeAndBytesMustBeValid annotation;
  
  @Mock
  private FileData fileData;
  
  @InjectMocks
  private TypeAndBytesMustBeValidValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, fileData);
  }
  
  @Test
  public void testGivenEmptyBytesAndFileTypeByValidatingFileWebRequestObjectReturnTrue() {
    
    when(fileData.getType()).thenReturn("FOLDER");
    when(fileData.getBytes()).thenReturn(null);
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNonEmptyBytesAndFileTypeByValidatingFileWebRequestObjectReturnTrue() {
    
    when(fileData.getType()).thenReturn("FILE");
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getType();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNonEmptyBytesAndInvalidFileTypeByValidatingFileWebRequestObjectReturnFalse() {
    
    when(fileData.getType()).thenReturn("FOLDER");
    when(fileData.getBytes()).thenReturn("sample".getBytes());
    
    assertThat(validator.isValid(fileData, null)).isFalse();
    
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenEmptyBytesAndInvalidFileTypeByValidatingFileWebRequestObjectReturnTrue() {
    
    when(fileData.getType()).thenReturn("FOLDER");
    when(fileData.getBytes()).thenReturn(new byte[0]);
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
}
