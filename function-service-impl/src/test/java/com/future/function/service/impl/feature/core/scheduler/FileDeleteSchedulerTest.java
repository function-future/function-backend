package com.future.function.service.impl.feature.core.scheduler;

import com.future.function.common.properties.core.FileProperties;
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
  
  private static final long MINIMUM_CREATED_PERIOD = 1800;
  
  private FileV2 file;
  
  @Mock
  private FileRepositoryV2 fileRepository;
  
  @Mock
  private FileProperties fileProperties;
  
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
  public void tearDown() {
    
    Mockito.verifyNoMoreInteractions(fileRepository, fileProperties);
  }
  
  @Test
  public void testGivenMethodCallByDeletingUnusedFileReturnSuccessfulDeletion()
    throws Exception {
    
    file.setCreatedAt(System.currentTimeMillis() - MINIMUM_CREATED_PERIOD * 2);
    
    Mockito.when(fileRepository.findAllByUsedFalse())
      .thenReturn(Stream.of(file));
    Mockito.when(fileProperties.getMinimumFileCreatedPeriod())
      .thenReturn(MINIMUM_CREATED_PERIOD);
    
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
    Mockito.verify(fileProperties)
      .getMinimumFileCreatedPeriod();
    Mockito.verify(fileRepository)
      .delete(file);
    
    PowerMockito.verifyNew(File.class)
      .withArguments(TRIMMED_PATH);
    
    PowerMockito.verifyStatic(FileSystemUtils.class);
    FileSystemUtils.deleteRecursively(fileMock);
  }
  
  @Test
  public void testGivenMethodCallByDeletingUnusedFolderReturnSuccessfulDeletionWithoutFileSystemUtilsCall() {
    
    file.setCreatedAt(System.currentTimeMillis() - MINIMUM_CREATED_PERIOD * 2);
    file.setMarkFolder(true);
    
    Mockito.when(fileRepository.findAllByUsedFalse())
      .thenReturn(Stream.of(file));
    Mockito.when(fileProperties.getMinimumFileCreatedPeriod())
      .thenReturn(MINIMUM_CREATED_PERIOD);
    
    PowerMockito.mockStatic(File.class);
    
    PowerMockito.mockStatic(FileSystemUtils.class);
    
    fileDeleteScheduler.deleteFileOnSchedule();
    
    Mockito.verify(fileRepository)
      .findAllByUsedFalse();
    Mockito.verify(fileProperties)
      .getMinimumFileCreatedPeriod();
    Mockito.verify(fileRepository)
      .delete(file);
    
    PowerMockito.verifyZeroInteractions(File.class, FileSystemUtils.class);
  }
  
  @Test
  public void testGivenMethodCallAndBefore30MinutesByDeletingUnusedFileReturnNoDeletion() {
    
    file.setCreatedAt(System.currentTimeMillis());
    
    Mockito.when(fileRepository.findAllByUsedFalse())
      .thenReturn(Stream.of(file));
    Mockito.when(fileProperties.getMinimumFileCreatedPeriod())
      .thenReturn(MINIMUM_CREATED_PERIOD);
    
    PowerMockito.mockStatic(File.class);
    PowerMockito.mockStatic(FileSystemUtils.class);
    
    fileDeleteScheduler.deleteFileOnSchedule();
    
    Mockito.verify(fileRepository)
      .findAllByUsedFalse();
    Mockito.verify(fileProperties)
      .getMinimumFileCreatedPeriod();
    
    PowerMockito.verifyZeroInteractions(File.class, FileSystemUtils.class);
  }
  
}
