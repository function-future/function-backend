package com.future.function.service.impl.feature.core.scheduler;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.stream.Stream;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {
  FileSystemUtils.class, File.class, FileDeleteScheduler.class
})
public class FileDeleteSchedulerTest {
  
  private static final String TRIMMED_PATH = "file";
  
  private static final String FILE_PATH =
    TRIMMED_PATH + File.separator + "path";
  
  private FileV2 file;
  
  @Mock
  private FileRepositoryV2 fileRepository;
  
  @InjectMocks
  private FileDeleteScheduler fileDeleteScheduler;
  
  @Before
  public void setUp() {
    
    file = FileV2.builder()
      .used(false)
      .filePath(FILE_PATH)
      .build();
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenMethodCallByDeletingUnusedFileReturnSuccessfulDeletion()
    throws Exception {
    
    file.setCreatedAt(
      System.currentTimeMillis() - FileDeleteScheduler.HALF_HOUR * 2);
    
    Mockito.when(fileRepository.findAllByUsedFalse())
      .thenReturn(Stream.of(file));
    
    PowerMockito.mockStatic(File.class);
    File fileMock = PowerMockito.mock(File.class);
    
    PowerMockito.whenNew(File.class)
      .withArguments(TRIMMED_PATH)
      .thenReturn(fileMock);
    
    PowerMockito.mockStatic(FileSystemUtils.class);
    PowerMockito.when(FileSystemUtils.deleteRecursively(fileMock))
      .thenReturn(true);
    
    fileDeleteScheduler.deleteFileOnSchedule();
    
    Mockito.verify(fileRepository)
      .findAllByUsedFalse();
    Mockito.verify(fileRepository)
      .delete(file);
    
    PowerMockito.verifyNew(File.class)
      .withArguments(TRIMMED_PATH);
    
    PowerMockito.verifyStatic(FileSystemUtils.class);
    FileSystemUtils.deleteRecursively(fileMock);
  }
  
  @Test
  public void testGivenMethodCallAndBefore30MinutesByDeletingUnusedFileReturnNoDeletion() {
    
    file.setCreatedAt(System.currentTimeMillis());
    
    Mockito.when(fileRepository.findAllByUsedFalse())
      .thenReturn(Stream.of(file));
    
    PowerMockito.mockStatic(File.class);
    PowerMockito.mockStatic(FileSystemUtils.class);
    
    fileDeleteScheduler.deleteFileOnSchedule();
    
    Mockito.verify(fileRepository)
      .findAllByUsedFalse();
    
    PowerMockito.verifyZeroInteractions(File.class, FileSystemUtils.class);
  }
  
}