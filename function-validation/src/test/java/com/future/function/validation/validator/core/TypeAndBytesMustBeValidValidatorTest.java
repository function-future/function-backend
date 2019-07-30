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
  public void testGivenEmptyBytesAndFileTypeFolderByValidatingFileWebRequestForCreatingFolderReturnTrue() {
    
    when(fileData.getId()).thenReturn(null);
    when(fileData.getType()).thenReturn("FOLDER");
    when(fileData.getBytes()).thenReturn(new byte[0]);
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getId();
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenNonEmptyBytesAndFileTypeFileByValidatingFileWebRequestForCreatingFileReturnTrue() {
    
    when(fileData.getId()).thenReturn(null);
    when(fileData.getType()).thenReturn("FILE");
    when(fileData.getBytes()).thenReturn(new byte[] { 1, 2, 3 });
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getId();
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenIdAndEmptyBytesAndFileTypeFolderByValidatingFileWebRequestForUpdatingFolderReturnTrue() {
    
    when(fileData.getId()).thenReturn("id");
    when(fileData.getType()).thenReturn("FOLDER");
    when(fileData.getBytes()).thenReturn(new byte[0]);
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getId();
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenIdAndNonEmptyBytesAndFileTypeFolderByValidatingFileWebRequestForUpdatingFolderReturnFalse() {
    
    when(fileData.getId()).thenReturn("id");
    when(fileData.getType()).thenReturn("FOLDER");
    when(fileData.getBytes()).thenReturn(new byte[] { 1, 2, 3 });
    
    assertThat(validator.isValid(fileData, null)).isFalse();
    
    verify(fileData).getId();
    verify(fileData).getType();
    verify(fileData).getBytes();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenIdAndNonEmptyBytesAndFileTypeFileByValidatingFileWebRequestForUpdatingFileReturnTrue() {
    
    when(fileData.getId()).thenReturn("id");
    when(fileData.getType()).thenReturn("FILE");
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getId();
    verify(fileData).getType();
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenIdAndEmptyBytesAndFileTypeFileByValidatingFileWebRequestForUpdatingFileReturnTrueToAllowChangeOfNameOnly() {
    
    when(fileData.getId()).thenReturn("id");
    when(fileData.getType()).thenReturn("FILE");
    
    assertThat(validator.isValid(fileData, null)).isTrue();
    
    verify(fileData).getId();
    verify(fileData).getType();
    verifyZeroInteractions(annotation);
  }
  
}
