package com.future.function.common.enumeration.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTypeTest {
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenMethodCallToGetFileTypeFromMarkFolderFlagByGettingFileTypeReturnFileType() {
    
    assertThat(FileType.getFileType(false)).isEqualTo(FileType.FILE.name());
    assertThat(FileType.getFileType(true)).isEqualTo(FileType.FOLDER.name());
  }
  
  @Test
  public void testGivenMethodCallToCheckIfFileTypeIsMarkedFolderFromFileTypeByCheckingMarkedFolderReturnBoolean() {
    
    assertThat(FileType.FILE.isMarkFolder()).isFalse();
    assertThat(FileType.FOLDER.isMarkFolder()).isTrue();
  }
  
}
