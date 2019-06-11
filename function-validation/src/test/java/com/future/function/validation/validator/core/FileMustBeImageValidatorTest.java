package com.future.function.validation.validator.core;

import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.validation.annotation.core.FileMustBeImage;
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
public class FileMustBeImageValidatorTest {
  
  private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
    ".png", ".jpg", ".jpeg");
  
  private static final String IMAGE_FILE_PATH = "C:\\file.png";
  
  private static final String TEXT_FILE_PATH = "C:\\file.txt";
  
  private static final String FILE_ID = "file-id";
  
  private static final FileV2 IMAGE_FILE = FileV2.builder()
    .id(FILE_ID)
    .filePath(IMAGE_FILE_PATH)
    .build();
  
  private static final FileV2 TEXT_FILE = FileV2.builder()
    .id(FILE_ID)
    .filePath(TEXT_FILE_PATH)
    .build();
  
  private static final List<String> FILE_IDS = Collections.singletonList(
    FILE_ID);
  
  @Mock
  private FileMustBeImage annotation;
  
  @Mock
  private FileProperties fileProperties;
  
  @Mock
  private FileRepositoryV2 fileRepositoryV2;
  
  @InjectMocks
  private FileMustBeImageValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
    
    when(fileProperties.getImageExtensions()).thenReturn(IMAGE_EXTENSIONS);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, fileProperties, fileRepositoryV2);
  }
  
  @Test
  public void testGivenListOfFileIdsOfImageFilesByValidatingFileIdsOfImageReturnTrue() {
    
    when(fileRepositoryV2.findOne(FILE_ID)).thenReturn(IMAGE_FILE);
    
    assertThat(validator.isValid(FILE_IDS, null)).isTrue();
    
    verify(fileProperties).getImageExtensions();
    verify(fileRepositoryV2).findOne(FILE_ID);
    verifyZeroInteractions(annotation);
  }
  
  @Test
  public void testGivenListOfFileIdsOfNonImageFilesByValidatingFileIdsOfImageReturnFalse() {
    
    when(fileRepositoryV2.findOne(FILE_ID)).thenReturn(TEXT_FILE);
    
    assertThat(validator.isValid(FILE_IDS, null)).isFalse();
    
    verify(fileProperties).getImageExtensions();
    verify(fileRepositoryV2).findOne(FILE_ID);
    verifyZeroInteractions(annotation);
  }
  
}