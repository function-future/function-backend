package com.future.function.service.impl.helper;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileHelper.class, File.class, FileUtils.class })
public class FileHelperTest {
  
  public static final String NAME = UUID.randomUUID()
    .toString();
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenJavaIoFileByConvertingToByteArrayReturnByteArray() {
    
    String testFilePath = Paths.get("src", "test", "resources", "TestFile.png")
      .toString();
    
    byte[] bytes = FileHelper.toByteArray(new File(testFilePath));
    
    assertThat(bytes.length).isNotEqualTo(0);
  }
  
  @Test
  public void testGivenInvalidJavaIoFileByConvertingToByteArrayReturnEmptyByteArray() {
    
    byte[] bytes = FileHelper.toByteArray(new File("test"));
    
    assertThat(bytes.length).isEqualTo(0);
  }
  
  @Test
  public void testGivenByteArrayAndPathByConvertingToFileReturnFileObject()
    throws Exception {
    
    mockStatic(File.class);
    File data = mock(File.class);
    
    String path = "path";
    whenNew(File.class).withArguments(path)
      .thenReturn(data);
    
    byte[] bytes = new byte[] {};
    
    mockStatic(FileUtils.class);
    doNothing().when(FileUtils.class, "writeByteArrayToFile", data, bytes);
    
    File file = FileHelper.toJavaIoFile(bytes, path);
    
    assertThat(file).isNotNull();
    
    verifyNew(File.class).withArguments(path);
    
    verifyStatic(FileUtils.class);
    FileUtils.writeByteArrayToFile(data, bytes);
  }
  
  @Test
  public void testGivenByteArrayAndPathButExceptionOccuredByConvertingToFileReturnNull()
    throws Exception {
    
    mockStatic(File.class);
    File data = mock(File.class);
    
    String path = "path";
    whenNew(File.class).withArguments(path)
      .thenReturn(data);
    
    byte[] bytes = new byte[] {};
    
    mockStatic(FileUtils.class);
    doThrow(new IOException()).when(
      FileUtils.class, "writeByteArrayToFile", data, bytes);
    
    File file = FileHelper.toJavaIoFile(bytes, path);
    
    assertThat(file).isNull();
    
    verifyNew(File.class).withArguments(path);
    
    verifyStatic(FileUtils.class);
    FileUtils.writeByteArrayToFile(data, bytes);
  }
  
  @Test
  public void testGivenNonThumbnailFileNameByCheckingIsThumbnailReturnFalse() {
    
    assertThat(FileHelper.isThumbnailName(NAME)).isFalse();
  }
  
  @Test
  public void testGivenThumbnailFileNameByCheckingIsThumbnailReturnTrue() {
    
    assertThat(FileHelper.isThumbnailName(NAME + "-thumbnail")).isTrue();
  }
  
}