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
@PrepareForTest({
                  FileSystemUtils.class, File.class, FileDeleteScheduler.class
                })
public class FileDeleteSchedulerTest {
  
  private static final String TRIMMED_PATH = "file";
  
  private static final String FILE_PATH =
    TRIMMED_PATH + File.separator + "path";
  
  private static final FileV2 FILE = FileV2.builder()
    .used(false)
    .filePath(FILE_PATH)
    .build();
  
  @Mock
  private FileRepositoryV2 fileRepository;
  
  @InjectMocks
  private FileDeleteScheduler fileDeleteScheduler;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenMethodCallByDeletingUnusedFileReturnSuccessfulDeletion()
    throws Exception {
    
    Mockito.when(fileRepository.findAllByUsedFalse())
      .thenReturn(Stream.of(FILE));
    
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
      .delete(FILE);
    
    PowerMockito.verifyNew(File.class)
      .withArguments(TRIMMED_PATH);
    
    PowerMockito.verifyStatic(FileSystemUtils.class);
    FileSystemUtils.deleteRecursively(fileMock);
  }
  
}