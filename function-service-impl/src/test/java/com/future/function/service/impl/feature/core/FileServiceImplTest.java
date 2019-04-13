package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.File;
import com.future.function.repository.feature.core.FileRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest {
  
  private static final String BASE_URL = "http://localhost:8080/files/resource";
  
  private static final String BASE_PATH = "C:\\function\\files\\static";
  
  private static final String ID = UUID.randomUUID()
    .toString();
  
  private static final String FOLDER_PATH =
    BASE_PATH + java.io.File.separator + ID;
  
  private static final String FILE_PATH =
    FOLDER_PATH + java.io.File.separator + ID + ".png";
  
  private static final String FILE_URL = BASE_URL + "/" + ID + ".png";
  
  private static final String THUMBNAIL_PATH =
    FOLDER_PATH + java.io.File.separator + ID + "-thumbnail.png";
  
  private static final String THUMBNAIL_URL =
    BASE_URL + "/" + ID + "-thumbnail.png";
  
  private File file;
  
  @Mock
  private FileRepository fileRepository;
  
  @Mock
  private FileProperties fileProperties;
  
  @InjectMocks
  private FileServiceImpl fileService;
  
  @Before
  public void setUp() {
  
    List<String> imageExtensions = Arrays.asList(".jpg", ".jpeg", ".png");
    when(fileProperties.getImageExtensions()).thenReturn(imageExtensions);
    when(fileProperties.getUrlPrefix()).thenReturn(BASE_URL);
    when(fileProperties.getStoragePath()).thenReturn(BASE_PATH);
    when(fileProperties.getThumbnailSuffix()).thenReturn("-thumbnail");
    
    file = File.builder()
      .id(ID)
      .filePath(FILE_PATH)
      .fileUrl(FILE_URL)
      .thumbnailPath(THUMBNAIL_PATH)
      .thumbnailUrl(THUMBNAIL_URL)
      .asResource(true)
      .build();
  }
  
  @After
  public void tearDown() {
    
    FileSystemUtils.deleteRecursively(Paths.get(FOLDER_PATH)
                                        .toFile());
    
    verifyNoMoreInteractions(fileRepository);
  }
  
  @Test
  public void testGivenFileWithExistingIdByGettingFileObjectReturnFileObject() {
    
    when(fileRepository.findOne(ID)).thenReturn(file);
    
    File foundFile = fileService.getFile(ID);
    
    assertThat(foundFile).isNotNull();
    assertThat(foundFile).isEqualTo(file);
    
    verify(fileRepository).findOne(ID);
  }
  
  @Test
  public void testGivenFileWithNonExistingIdByGettingFileObjectReturnNotFoundException() {
    
    when(fileRepository.findOne(ID)).thenReturn(null);
    
    try {
      fileService.getFile(ID);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Get File Not Found");
    }
    
    verify(fileRepository).findOne(ID);
  }
  
}
