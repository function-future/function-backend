package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.ResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest {
  
  private static final String ID = "id";
  
  private static final String PARENT_ID = "parent-id";
  
  private static final FileV2 FILE = FileV2.builder()
    .id(ID)
    .parentId(PARENT_ID)
    .markFolder(false)
    .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 5);
  
  private static final Page<FileV2> PAGE = new PageImpl<>(
    Collections.singletonList(FILE), PAGEABLE, 1);
  
  private static final String ROOT = "root";
  
  private static final String EMAIL = "email";
  
  private static final String NAME = "name";
  
  @Mock
  private FileRepositoryV2 fileRepository;
  
  @Mock
  private ResourceService resourceService;
  
  @Mock
  private FileProperties fileProperties;
  
  @InjectMocks
  private FileServiceImpl fileService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(fileRepository, resourceService, fileProperties);
  }
  
  @Test
  public void testGivenFileOrFolderIdAndParentIdByGettingFileOrFolderReturnFileOrFolder() {
    
    when(fileRepository.findByIdAndParentId(ID, PARENT_ID)).thenReturn(
      Optional.of(FILE));
    
    FileV2 file = fileService.getFileOrFolder(ID, PARENT_ID);
    
    assertThat(file).isNotNull();
    assertThat(file).isEqualTo(FILE);
    
    verify(fileRepository).findByIdAndParentId(ID, PARENT_ID);
    verifyZeroInteractions(resourceService, fileProperties);
  }
  
  @Test
  public void testGivenInvalidFileOrFolderIdAndParentIdByGettingFileOrFolderReturnNotFoundException() {
    
    when(fileRepository.findByIdAndParentId(ID, PARENT_ID)).thenReturn(
      Optional.empty());
    
    catchException(() -> fileService.getFileOrFolder(ID, PARENT_ID));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get File/Folder Not Found");
    
    verify(fileRepository).findByIdAndParentId(ID, PARENT_ID);
    verifyZeroInteractions(resourceService, fileProperties);
  }
  
  @Test
  public void testGivenParentIdAndPageableByGettingFilesOrFoldersReturnPageOfFile() {
    
    when(
      fileRepository.findAllByParentIdAndAsResourceFalseOrderByMarkFolderDesc(
        PARENT_ID, PAGEABLE)).thenReturn(PAGE);
    
    Page<FileV2> page = fileService.getFilesAndFolders(PARENT_ID, PAGEABLE);
    
    assertThat(page).isNotNull();
    assertThat(page).isEqualTo(PAGE);
    
    verify(
      fileRepository).findAllByParentIdAndAsResourceFalseOrderByMarkFolderDesc(
      PARENT_ID, PAGEABLE);
  }
  
  @Test
  public void testGivenEmailAndParentIdAndFileOrFolderIdByDeletingFileOrFolderReturnSuccessfulDeletion() {
    
    when(fileProperties.getRootId()).thenReturn(ROOT);
    when(fileRepository.findByIdAndParentId(ID, PARENT_ID)).thenReturn(
      Optional.of(FILE));
    when(fileRepository.findAllByParentId(ID)).thenReturn(Stream.empty());
    when(resourceService.markFilesUsed(Collections.singletonList(ID),
                                       false
    )).thenReturn(true);
    
    fileService.deleteFileOrFolder(EMAIL, PARENT_ID, ID);
    
    verify(fileProperties).getRootId();
    verify(fileRepository).findByIdAndParentId(ID, PARENT_ID);
    verify(fileRepository).findAllByParentId(ID);
    verify(resourceService).markFilesUsed(Collections.singletonList(ID), false);
  }
  
  @Test
  public void testGivenInvalidEmailAndParentIdAndFileOrFolderIdByDeletingFileOrFolderReturnFailedDeletion() {
    
    when(fileProperties.getRootId()).thenReturn(ROOT);
    
    fileService.deleteFileOrFolder(EMAIL, PARENT_ID, ROOT);
    
    verify(fileProperties).getRootId();
    verifyZeroInteractions(fileRepository, resourceService);
  }
  
  @Test
  public void testGivenMethodCallAndNonEmptyByteArrayByCreatingFileOrFolderReturnNewFile() {
    
    FileV2 returnedFile = FileV2.builder()
      .parentId(PARENT_ID)
      .name(NAME)
      .build();
    when(resourceService.storeFile(null, PARENT_ID, NAME, NAME, NAME.getBytes(),
                                   FileOrigin.FILE
    )).thenReturn(returnedFile);
    
    FileV2 savedFile = FileV2.builder()
      .parentId(PARENT_ID)
      .name(NAME)
      .used(true)
      .build();
    when(fileRepository.save(any(FileV2.class))).thenReturn(savedFile);
    
    FileV2 createdFile = fileService.createFileOrFolder(
      PARENT_ID, NAME, NAME, NAME.getBytes());
    
    assertThat(createdFile).isNotNull();
    assertThat(createdFile.getId()).isNotBlank();
    assertThat(createdFile.getName()).isEqualTo(NAME);
    assertThat(createdFile.getParentId()).isEqualTo(PARENT_ID);
    assertThat(createdFile.isUsed()).isTrue();
    assertThat(createdFile.isMarkFolder()).isFalse();
    
    verify(resourceService).storeFile(
      null, PARENT_ID, NAME, NAME, NAME.getBytes(), FileOrigin.FILE);
    verify(fileRepository).save(any(FileV2.class));
    verifyZeroInteractions(fileProperties);
  }
  
  @Test
  public void testGivenMethodCallAndEmptyByteArrayByCreatingFileOrFolderReturnNewFolder() {
    
    FileV2 returnedFolder = FileV2.builder()
      .name(NAME)
      .parentId(PARENT_ID)
      .used(true)
      .markFolder(true)
      .build();
    
    when(fileRepository.save(any(FileV2.class))).thenReturn(returnedFolder);
    
    FileV2 createdFolder = fileService.createFileOrFolder(PARENT_ID, NAME, NAME,
                                                          new byte[] {}
    );
    
    assertThat(createdFolder).isNotNull();
    assertThat(createdFolder.getId()).isNotBlank();
    assertThat(createdFolder.getName()).isEqualTo(NAME);
    assertThat(createdFolder.getParentId()).isEqualTo(PARENT_ID);
    assertThat(createdFolder.isUsed()).isTrue();
    assertThat(createdFolder.isMarkFolder()).isTrue();
    
    verify(fileRepository).save(any(FileV2.class));
    verifyZeroInteractions(resourceService, fileProperties);
  }
  
  @Test
  public void testGivenMethodCallAndNonEmptyByteArrayByUpdatingFileOrFolderReturnUpdatedFile() {
    
    when(fileRepository.findByIdAndParentId(ID, PARENT_ID)).thenReturn(
      Optional.of(FILE));
    when(resourceService.storeFile(ID, PARENT_ID, NAME, NAME, NAME.getBytes(),
                                   FileOrigin.FILE
    )).thenReturn(FILE);
    when(fileRepository.findOne(ID)).thenReturn(FILE);
    when(fileRepository.save(FILE)).thenReturn(FILE);
    
    FileV2 updatedFile = fileService.updateFileOrFolder(
      EMAIL, ID, PARENT_ID, NAME, NAME, NAME.getBytes());
    
    assertThat(updatedFile).isNotNull();
    assertThat(updatedFile).isEqualTo(FILE);
    
    verify(fileRepository).findByIdAndParentId(ID, PARENT_ID);
    verify(resourceService).storeFile(
      ID, PARENT_ID, NAME, NAME, NAME.getBytes(), FileOrigin.FILE);
    verify(fileRepository).findOne(ID);
    verify(fileRepository).save(FILE);
    verifyZeroInteractions(fileProperties);
  }
  
  @Test
  public void testGivenMethodCallAndEmptyByteArrayByUpdatingFileOrFolderReturnUpdatedFolder() {
    
    FileV2 folder = FileV2.builder()
      .name(NAME)
      .parentId(PARENT_ID)
      .used(true)
      .markFolder(true)
      .build();
    
    when(
      fileRepository.findByIdAndParentId(folder.getId(), PARENT_ID)).thenReturn(
      Optional.of(folder));
    when(fileRepository.save(folder)).thenReturn(folder);
    
    FileV2 updatedFolder = fileService.updateFileOrFolder(
      EMAIL, folder.getId(), PARENT_ID, NAME, NAME, new byte[] {});
    
    assertThat(updatedFolder).isNotNull();
    assertThat(updatedFolder).isEqualTo(folder);
    
    verify(fileRepository).findByIdAndParentId(folder.getId(), PARENT_ID);
    verify(fileRepository).save(folder);
    verifyZeroInteractions(resourceService, fileProperties);
  }
  
}
