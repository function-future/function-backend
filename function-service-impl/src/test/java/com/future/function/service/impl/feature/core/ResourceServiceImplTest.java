package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.Version;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.impl.helper.FileHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ File.class, FileHelper.class })
public class ResourceServiceImplTest {
  
  private static final String FILE_ID = "file-id";
  
  private static final String FILE_PATH = "file-path";
  
  private static final String FILE_URL = "file-url";
  
  private static final String THUMBNAIL_PATH = "thumbnail-path";
  
  private static final FileV2 FILE_V2 = FileV2.builder()
    .id(FILE_ID)
    .filePath(FILE_PATH)
    .thumbnailPath(THUMBNAIL_PATH)
    .asResource(FileOrigin.ANNOUNCEMENT.isAsResource())
    .versions(Collections.singletonMap(1L,
                                       new Version(System.currentTimeMillis(),
                                                   FILE_PATH, FILE_URL
                                       )
    ))
    .build();
  
  private static final String THUMBNAIL_SUFFIX = "-thumbnail";
  
  private static final byte[] BYTES = new byte[0];
  
  private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
    ".png", ".jpg", ".jpeg");
  
  private static FileProperties fileProperties;
  
  @Mock
  private FileRepositoryV2 fileRepositoryV2;
  
  @InjectMocks
  private ResourceServiceImpl resourceService;
  
  @BeforeClass
  public static void setUpClass() {
    
    fileProperties = Mockito.mock(FileProperties.class);
    
    when(fileProperties.getImageExtensions()).thenReturn(IMAGE_EXTENSIONS);
    when(fileProperties.getStoragePath()).thenReturn("");
    when(fileProperties.getThumbnailSuffix()).thenReturn(THUMBNAIL_SUFFIX);
  }
  
  @AfterClass
  public static void tearDownClass() {
    
    final int numberOfTestMethodInClass = 12;
    
    verify(
      fileProperties, times(numberOfTestMethodInClass)).getImageExtensions();
    verify(fileProperties, times(numberOfTestMethodInClass)).getStoragePath();
    verify(
      fileProperties, times(numberOfTestMethodInClass)).getThumbnailSuffix();
    
    verifyNoMoreInteractions(fileProperties);
  }
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(fileRepositoryV2);
  }
  
  @Test
  public void testGivenFileIdByGettingFileReturnFileV2Object() {
    
    when(fileRepositoryV2.findOne(FILE_ID)).thenReturn(FILE_V2);
    
    FileV2 fileV2 = resourceService.getFile(FILE_ID);
    
    assertThat(fileV2).isNotNull();
    assertThat(fileV2).isEqualTo(FILE_V2);
    
    verify(fileRepositoryV2).findOne(FILE_ID);
  }
  
  @Test
  public void testGivenNonExistingFileIdByGettingFileReturnFileV2Object() {
    
    when(fileRepositoryV2.findOne(FILE_ID)).thenReturn(null);
    
    FileV2 fileV2 = resourceService.getFile(FILE_ID);
    
    assertThat(fileV2).isNull();
    
    verify(fileRepositoryV2).findOne(FILE_ID);
  }
  
  @Test
  public void testGivenFileIdsAndUsedMarkingByMarkingFilesUsedReturnTrue() {
    
    FileV2 fileV2 = FileV2.builder()
      .id(FILE_ID)
      .used(false)
      .build();
    
    List<String> fileIdList = Collections.singletonList(FILE_ID);
    List<FileV2> fileV2List = Collections.singletonList(fileV2);
    
    when(fileRepositoryV2.findAll(fileIdList)).thenReturn(fileV2List);
    
    fileV2.setUsed(true);
    when(fileRepositoryV2.save(fileV2)).thenReturn(fileV2);
    
    assertThat(resourceService.markFilesUsed(fileIdList, true)).isTrue();
    
    verify(fileRepositoryV2).findAll(fileIdList);
    verify(fileRepositoryV2).save(fileV2);
  }
  
  @Test
  public void testGivenNonExistingFileIdsAndUsedMarkingByMarkingFilesUsedReturnNotFoundException() {
    
    List<String> fileIdList = Collections.singletonList(FILE_ID);
    List<FileV2> fileV2List = Collections.emptyList();
    
    when(fileRepositoryV2.findAll(fileIdList)).thenReturn(fileV2List);
    
    catchException(() -> resourceService.markFilesUsed(fileIdList, true));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("File Not Found");
    
    verify(fileRepositoryV2).findAll(fileIdList);
  }
  
  @Test
  public void testGivenMethodCallToStoreFileByStoringFileReturnFileV2Object()
    throws Exception {
    
    String fileId = UUID.randomUUID()
      .toString();
    String fileName = fileId + ".doc";
    
    File file = mock(File.class);
    
    mockStatic(File.class);
    whenNew(File.class).withArguments(FILE_PATH)
      .thenReturn(file);
    
    mockStatic(FileHelper.class);
    doNothing().when(FileHelper.class, "createJavaIoFile", BYTES, FILE_PATH);
    
    FileV2 expectedFileV2 = FileV2.builder()
      .name("")
      .asResource(true)
      .build();
    when(fileRepositoryV2.save(any(FileV2.class))).thenReturn(expectedFileV2);
    FileV2 fileV2 = resourceService.storeAndSaveFile("", fileName, BYTES,
                                                     FileOrigin.ANNOUNCEMENT
    );
    
    assertThat(fileV2.getId()).isNotBlank();
    assertThat(fileV2.getName()).isEqualTo(expectedFileV2.getName());
    assertThat(fileV2.getThumbnailPath()).isEmpty();
    assertThat(fileV2.getThumbnailUrl()).isNull();
    
    verify(fileRepositoryV2).save(any(FileV2.class));
  }
  
  @Test
  public void testGivenMethodCallAndFileIdAndParentIdToStoreFileByStoringFileReturnFileV2Object()
    throws Exception {
    
    File file = mock(File.class);
    
    mockStatic(File.class);
    whenNew(File.class).withArguments(FILE_PATH)
      .thenReturn(file);
    
    mockStatic(FileHelper.class);
    doNothing().when(FileHelper.class, "createJavaIoFile", BYTES, FILE_PATH);
    
    String fileId = UUID.randomUUID()
      .toString();
    String fileName = fileId + ".doc";
    String parentId = "parent-id";
    
    FileV2 returnedFileV2 = FileV2.builder()
      .id(fileId)
      .name("")
      .asResource(true)
      .parentId(parentId)
      .filePath("")
      .fileUrl("")
      .build();
    
    when(fileRepositoryV2.findOne(fileId)).thenReturn(returnedFileV2);
  
    FileV2 expectedFileV2 = FileV2.builder()
      .id(fileId)
      .name("")
      .asResource(true)
      .parentId(parentId)
      .filePath("\\announcement\\" + fileId + "-0\\" + fileName)
      .fileUrl("/announcement/" + fileName)
      .build();
    
    FileV2 fileV2 = resourceService.storeFile(fileId, parentId, "", fileName,
                                              BYTES, FileOrigin.ANNOUNCEMENT
    );
    
    assertThat(fileV2).isEqualTo(expectedFileV2);
    
    verify(fileRepositoryV2).findOne(fileId);
  }
  
  @Test
  public void testGivenMethodCallToStoreThumbnailFileByStoringFileReturnFileV2Object()
    throws Exception {
    
    String fileId = UUID.randomUUID()
      .toString();
    String fileName = fileId + ".png";
    
    File file = mock(File.class);
    
    mockStatic(File.class);
    whenNew(File.class).withArguments(FILE_PATH)
      .thenReturn(file);
    
    mockStatic(FileHelper.class);
    doNothing().when(FileHelper.class, "createJavaIoFile", BYTES, FILE_PATH);
    doNothing().when(FileHelper.class, "createThumbnail", eq(BYTES),
                     any(String.class), eq("png")
    );
    
    FileV2 expectedFileV2 = FileV2.builder()
      .name("")
      .asResource(true)
      .build();
    when(fileRepositoryV2.save(any(FileV2.class))).thenReturn(expectedFileV2);
    FileV2 fileV2 = resourceService.storeAndSaveFile("", fileName, BYTES,
                                                     FileOrigin.ANNOUNCEMENT
    );
    
    assertThat(fileV2.getId()).isNotBlank();
    assertThat(fileV2.getName()).isEqualTo(expectedFileV2.getName());
    assertThat(fileV2.getThumbnailPath()).isNotBlank();
    assertThat(fileV2.getThumbnailUrl()).isNotBlank();
    
    verify(fileRepositoryV2).save(any(FileV2.class));
  }
  
  @Test
  public void testGivenFileNameAndOriginByGettingFileAsByteArrayReturnByteArray()
    throws Exception {
    
    String fileId = UUID.randomUUID()
      .toString();
    when(fileRepositoryV2.findByIdAndAsResource(fileId,
                                                FileOrigin.BLOG.isAsResource()
    )).thenReturn(Optional.of(FILE_V2));
    
    File file = mock(File.class);
    
    mockStatic(File.class);
    whenNew(File.class).withArguments(FILE_PATH)
      .thenReturn(file);
    
    mockStatic(FileHelper.class);
    doReturn(BYTES).when(FileHelper.class, "toByteArray", any(File.class));
    
    String fileName = fileId + ".png";
    doReturn(false).when(FileHelper.class, "isThumbnailName", fileName);
    
    byte[] bytes = resourceService.getFileAsByteArray(
      Role.UNKNOWN, fileName, FileOrigin.BLOG, null);
    
    assertThat(bytes).isEqualTo(BYTES);
    
    verify(fileRepositoryV2).findByIdAndAsResource(fileId,
                                                   FileOrigin.BLOG.isAsResource()
    );
  }
  
  @Test
  public void testGivenFileNameAndOriginAndVersionByGettingFileAsByteArrayReturnByteArray()
    throws Exception {
    
    String fileId = UUID.randomUUID()
      .toString();
    when(fileRepositoryV2.findByIdAndAsResource(fileId,
                                                FileOrigin.ANNOUNCEMENT.isAsResource()
    )).thenReturn(Optional.of(FILE_V2));
    
    File file = mock(File.class);
    
    mockStatic(File.class);
    whenNew(File.class).withArguments(FILE_PATH)
      .thenReturn(file);
    
    mockStatic(FileHelper.class);
    doReturn(BYTES).when(FileHelper.class, "toByteArray", any(File.class));
    
    String fileName = fileId + ".png";
    doReturn(false).when(FileHelper.class, "isThumbnailName", fileName);
    
    byte[] bytes = resourceService.getFileAsByteArray(
      Role.UNKNOWN, fileName, FileOrigin.ANNOUNCEMENT, 1L);
    
    assertThat(bytes).isEqualTo(BYTES);
    
    verify(fileRepositoryV2).findByIdAndAsResource(
      fileId, FileOrigin.ANNOUNCEMENT.isAsResource());
  }
  
  @Test
  public void testGivenThumbnailFileNameAndOriginByGettingFileAsByteArrayReturnByteArray()
    throws Exception {
    
    String fileId = UUID.randomUUID()
      .toString();
    when(fileRepositoryV2.findByIdAndAsResource(fileId,
                                                FileOrigin.COURSE.isAsResource()
    )).thenReturn(Optional.of(FILE_V2));
    
    File file = mock(File.class);
    
    mockStatic(File.class);
    whenNew(File.class).withArguments(THUMBNAIL_PATH)
      .thenReturn(file);
    
    mockStatic(FileHelper.class);
    doReturn(BYTES).when(FileHelper.class, "toByteArray", any(File.class));
    
    String fileName = fileId + THUMBNAIL_SUFFIX + ".png";
    doReturn(true).when(FileHelper.class, "isThumbnailName", fileName);
    
    byte[] bytes = resourceService.getFileAsByteArray(
      Role.ADMIN, fileName, FileOrigin.COURSE, null);
    
    assertThat(bytes).isEqualTo(BYTES);
    
    verify(fileRepositoryV2).findByIdAndAsResource(
      fileId, FileOrigin.COURSE.isAsResource());
  }
  
  @Test
  public void testGivenNotFoundFileByGettingFileAsByteArrayReturnNotFoundException() {
    
    String fileId = UUID.randomUUID()
      .toString();
    when(fileRepositoryV2.findByIdAndAsResource(fileId,
                                                FileOrigin.COURSE.isAsResource()
    )).thenReturn(Optional.empty());
    
    String fileName = fileId + ".png";
    catchException(
      () -> resourceService.getFileAsByteArray(Role.ADMIN, fileName,
                                               FileOrigin.COURSE, null
      ));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get File Not Found");
    
    verify(fileRepositoryV2).findByIdAndAsResource(
      fileId, FileOrigin.COURSE.isAsResource());
  }
  
  @Test
  public void testGivenInvalidRoleByGettingFileAsByteArrayReturnUnauthorizedException() {
    
    String fileId = UUID.randomUUID()
      .toString();
    
    String fileName = fileId + ".png";
    catchException(
      () -> resourceService.getFileAsByteArray(Role.UNKNOWN, fileName,
                                               FileOrigin.COURSE, null
      ));
    
    assertThat(caughtException().getClass()).isEqualTo(UnauthorizedException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid Session for Guest");
    
    verifyZeroInteractions(fileRepositoryV2);
  }
  
}
