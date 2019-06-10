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
    
    when(fileData.getBytes()).thenReturn(new byte[] {});
    when(fileData.getType()).thenReturn("FILE");
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getBytes();
    verify(fileData).getType();
  }
  
  @Test
  public void testGivenNonEmptyBytesAndFileTypeByValidatingFileWebRequestObjectReturnTrue() {
    
    when(fileData.getBytes()).thenReturn("sample".getBytes());
    when(fileData.getType()).thenReturn("FILE");
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getBytes();
    verify(fileData).getType();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNonEmptyBytesAndInvalidFileTypeByValidatingFileWebRequestObjectReturnTrue() {
    
    when(fileData.getBytes()).thenReturn("sample".getBytes());
    when(fileData.getType()).thenReturn("FOLDER");
    
    assertThat(validator.isValid(fileData, null)).isFalse();
    
    verify(fileData).getBytes();
    verify(fileData).getType();
    verifyZeroInteractions(annotation);
  }
  
}
