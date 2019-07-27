package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.model.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest {

  private static final String ID = "id";

  private static final String PARENT_ID = "parent-id";

  private static final String EMAIL = "email";

  private static final Pageable PAGEABLE = new PageRequest(0, 5);

  private static final String ROOT = "root";

  private static final String NAME = "name";

  private static final Session SESSION = new Session("session-id", "user-id",
                                                     "batch-id", EMAIL,
                                                     Role.ADMIN
  );

  private FileV2 file = FileV2.builder()
    .id(ID)
    .parentId(PARENT_ID)
    .markFolder(false)
    .build();

  @Mock
  private FileRepositoryV2 fileRepository;

  @Mock
  private ResourceService resourceService;

  @Mock
  private FileProperties fileProperties;
  
  @Mock
  private UserService userService;

  @InjectMocks
  private FileServiceImpl fileService;

  @Before
  public void setUp() {

    file.setCreatedBy(EMAIL);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(fileRepository, resourceService, fileProperties,
                             userService
    );
  }

  @Test
  public void testGivenFileOrFolderIdAndParentIdByGettingFileOrFolderReturnFileOrFolder() {

    when(fileRepository.findByIdAndParentIdAndDeletedFalse(ID,
                                                           PARENT_ID
    )).thenReturn(Optional.of(file));

    FileV2 file = fileService.getFileOrFolder(ID, PARENT_ID);

    assertThat(file).isNotNull();
    assertThat(file).isEqualTo(this.file);

    verify(fileRepository).findByIdAndParentIdAndDeletedFalse(ID, PARENT_ID);
    verifyZeroInteractions(resourceService, fileProperties, userService);
  }

  @Test
  public void testGivenInvalidFileOrFolderIdAndParentIdByGettingFileOrFolderReturnNotFoundException() {

    when(fileRepository.findByIdAndParentIdAndDeletedFalse(ID,
                                                           PARENT_ID
    )).thenReturn(Optional.empty());

    catchException(() -> fileService.getFileOrFolder(ID, PARENT_ID));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get File/Folder Not Found");

    verify(fileRepository).findByIdAndParentIdAndDeletedFalse(ID, PARENT_ID);
    verifyZeroInteractions(resourceService, fileProperties, userService);
  }

  @Test
  public void testGivenParentIdAndPageableByGettingFilesOrFoldersReturnPageOfFile() {

    Page<FileV2> returnedPage = new PageImpl<>(
      Collections.singletonList(file), PAGEABLE, 1);

    when(
      fileRepository.findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDesc(
        PARENT_ID, PAGEABLE)).thenReturn(returnedPage);
    
    FileV2 parentObject = FileV2.builder()
      .id(PARENT_ID)
      .build();
    when(fileRepository.findByIdAndDeletedFalse(PARENT_ID)).thenReturn(
      Optional.of(parentObject));
  
    Pair<List<FileV2>, Page<FileV2>> pathsAndFilesOrFolders =
      fileService.getFilesAndFolders(PARENT_ID, PAGEABLE);

    List<FileV2> paths = pathsAndFilesOrFolders.getFirst();
    
    assertThat(paths).isNotNull();
    assertThat(paths).isEqualTo(Collections.singletonList(parentObject));
    
    Page<FileV2> page = pathsAndFilesOrFolders
      .getSecond();

    assertThat(page).isNotNull();
    assertThat(page).isEqualTo(
      new PageImpl<>(Collections.singletonList(file), PAGEABLE, 1));

    verify(
      fileRepository).findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDesc(
      PARENT_ID, PAGEABLE);
    verify(fileRepository).findByIdAndDeletedFalse(PARENT_ID);
    verifyZeroInteractions(resourceService, fileProperties, userService);
  }

  @Test
  public void testGivenEmailAndParentIdAndFileOrFolderIdByDeletingFileOrFolderReturnSuccessfulDeletion() {

    when(fileProperties.getRootId()).thenReturn(ROOT);
    when(fileRepository.findByIdAndParentIdAndDeletedFalse(ID,
                                                           PARENT_ID
    )).thenReturn(Optional.of(file));
    when(fileRepository.findAllByParentIdAndDeletedFalse(ID)).thenReturn(
      Collections.emptyList());
    when(resourceService.markFilesUsed(Collections.singletonList(ID),
                                       false
    )).thenReturn(true);
    when(fileRepository.findAll(Collections.singletonList(ID))).thenReturn(
      Collections.singletonList(file));

    FileV2 markedDeletedFile = new FileV2();
    BeanUtils.copyProperties(file, markedDeletedFile);
    markedDeletedFile.setDeleted(true);
    when(fileRepository.save(
      Collections.singletonList(markedDeletedFile))).thenReturn(
      Collections.singletonList(markedDeletedFile));

    fileService.deleteFileOrFolder(SESSION, PARENT_ID, ID);

    verify(fileProperties).getRootId();
    verify(fileRepository).findByIdAndParentIdAndDeletedFalse(ID, PARENT_ID);
    verify(fileRepository).findAllByParentIdAndDeletedFalse(ID);
    verify(resourceService).markFilesUsed(Collections.singletonList(ID), false);
    verify(fileRepository).findAll(Collections.singletonList(ID));
    verify(fileRepository).save(Collections.singletonList(markedDeletedFile));
    verifyZeroInteractions(userService);
  }

  @Test
  public void testGivenEmailAndParentIdAndFileOrFolderIdAndNestedFileOrFolderByDeletingFileOrFolderReturnSuccessfulDeletion() {

    FileV2 folder1 = FileV2.builder()
      .id("folder-1")
      .parentId(ROOT)
      .markFolder(true)
      .build();
    FileV2 file1 = FileV2.builder()
      .id("file-1")
      .parentId(folder1.getId())
      .build();
    FileV2 folder2 = FileV2.builder()
      .id("folder-2")
      .parentId(folder1.getId())
      .markFolder(true)
      .build();
    FileV2 file2 = FileV2.builder()
      .id("file-2")
      .parentId(folder2.getId())
      .build();
    FileV2 file3 = FileV2.builder()
      .id("file-3")
      .parentId(folder2.getId())
      .build();

    when(fileProperties.getRootId()).thenReturn(ROOT);

    when(fileRepository.findByIdAndParentIdAndDeletedFalse(folder1.getId(),
                                                           folder1.getParentId()
    )).thenReturn(Optional.of(folder1));
    when(fileRepository.findAllByParentIdAndDeletedFalse(
      folder1.getId())).thenReturn(Arrays.asList(file1, folder2));

    when(fileRepository.findAllByParentIdAndDeletedFalse(
      file1.getId())).thenReturn(Collections.emptyList());
    when(fileRepository.findAllByParentIdAndDeletedFalse(
      folder2.getId())).thenReturn(Arrays.asList(file2, file3));
    when(fileRepository.findAllByParentIdAndDeletedFalse(
      file2.getId())).thenReturn(Collections.emptyList());
    when(fileRepository.findAllByParentIdAndDeletedFalse(
      file3.getId())).thenReturn(Collections.emptyList());

    List<String> fileIds = Arrays.asList(folder2.getId(), folder1.getId(),
                                         file1.getId(), file2.getId(),
                                         file3.getId()
    );
    when(resourceService.markFilesUsed(fileIds, false)).thenReturn(true);

    List<FileV2> fileV2s = Arrays.asList(folder2, folder1, file1, file2, file3);
    when(fileRepository.findAll(fileIds)).thenReturn(fileV2s);

    folder2.setDeleted(true);
    folder1.setDeleted(true);
    file1.setDeleted(true);
    file2.setDeleted(true);
    file3.setDeleted(true);
    when(fileRepository.save(fileV2s)).thenReturn(fileV2s);

    fileService.deleteFileOrFolder(SESSION, ROOT, folder1.getId());

    verify(fileProperties).getRootId();

    verify(fileRepository).findByIdAndParentIdAndDeletedFalse(
      folder1.getId(), folder1.getParentId());
    verify(fileRepository).findAllByParentIdAndDeletedFalse(folder1.getId());
    verify(fileRepository).findAllByParentIdAndDeletedFalse(file1.getId());
    verify(fileRepository).findAllByParentIdAndDeletedFalse(folder2.getId());
    verify(fileRepository).findAllByParentIdAndDeletedFalse(file2.getId());
    verify(fileRepository).findAllByParentIdAndDeletedFalse(file3.getId());

    verify(resourceService).markFilesUsed(fileIds, false);

    verify(fileRepository).findAll(fileIds);
    verify(fileRepository).save(fileV2s);
    
    verifyZeroInteractions(userService);
  }

  @Test
  public void testGivenInvalidEmailAndParentIdAndFileOrFolderIdByDeletingFileOrFolderReturnFailedDeletion() {

    when(fileProperties.getRootId()).thenReturn(ROOT);

    fileService.deleteFileOrFolder(SESSION, PARENT_ID, ROOT);

    verify(fileProperties).getRootId();
    verifyZeroInteractions(fileRepository, resourceService, userService);
  }

  @Test
  public void testGivenMethodCallAndNonEmptyByteArrayByCreatingFileReturnNewFile() {

    FileV2 returnedFile = FileV2.builder()
      .parentId(PARENT_ID)
      .name(NAME)
      .build();
    when(resourceService.storeFile(null, PARENT_ID, NAME, NAME, NAME.getBytes(),
                                   FileOrigin.FILE
    )).thenReturn(returnedFile);
  
    String userId = "user-id";
    when(userService.getUser(userId)).thenReturn(new User());

    FileV2 savedFile = FileV2.builder()
      .parentId(PARENT_ID)
      .name(NAME)
      .used(true)
      .user(new User())
      .build();
  
    when(fileRepository.findByIdAndDeletedFalse(PARENT_ID)).thenReturn(
      Optional.empty());
    
    when(fileRepository.save(any(FileV2.class))).thenReturn(savedFile);

    when(fileRepository.findByIdAndDeletedFalse(savedFile.getId())).thenReturn(
      Optional.of(savedFile));

    FileV2 createdFile = fileService.createFileOrFolder(
      new Session("", userId, "", EMAIL, Role.ADMIN), PARENT_ID, NAME, NAME,
      NAME.getBytes()
    );

    assertThat(createdFile).isNotNull();
    assertThat(createdFile.getId()).isNotBlank();
    assertThat(createdFile.getName()).isEqualTo(NAME);
    assertThat(createdFile.getParentId()).isEqualTo(PARENT_ID);
    assertThat(createdFile.isUsed()).isTrue();
    assertThat(createdFile.isMarkFolder()).isFalse();
    assertThat(createdFile.getUser()).isNotNull();
    assertThat(createdFile.getPaths()).isEqualTo(
      Collections.singletonList(savedFile));

    verify(resourceService).storeFile(
      null, PARENT_ID, NAME, NAME, NAME.getBytes(), FileOrigin.FILE);
    verify(userService).getUser(userId);
    verify(fileRepository).findByIdAndDeletedFalse(PARENT_ID);
    verify(fileRepository).findByIdAndDeletedFalse(savedFile.getId());
    verify(fileRepository, times(2)).save(any(FileV2.class));
    verifyZeroInteractions(fileProperties);
  }

  @Test
  public void testGivenMethodCallAndEmptyByteArrayByCreatingFolderReturnNewFolder() {
  
    String userId = "";
    when(userService.getUser(userId)).thenReturn(new User());

    FileV2 returnedFolder = FileV2.builder()
      .name(NAME)
      .parentId(PARENT_ID)
      .used(true)
      .markFolder(true)
      .user(new User())
      .build();
  
    when(fileRepository.findByIdAndDeletedFalse(PARENT_ID)).thenReturn(
      Optional.empty());
    when(fileRepository.findByIdAndDeletedFalse(
      returnedFolder.getId())).thenReturn(Optional.of(returnedFolder));

    when(fileRepository.save(any(FileV2.class))).thenReturn(returnedFolder);

    FileV2 createdFolder = fileService.createFileOrFolder(
      new Session("", userId, "", EMAIL, Role.ADMIN), PARENT_ID, NAME, NAME,
      new byte[] {}
    );

    assertThat(createdFolder).isNotNull();
    assertThat(createdFolder.getId()).isNotBlank();
    assertThat(createdFolder.getName()).isEqualTo(NAME);
    assertThat(createdFolder.getParentId()).isEqualTo(PARENT_ID);
    assertThat(createdFolder.isUsed()).isTrue();
    assertThat(createdFolder.isMarkFolder()).isTrue();
    assertThat(createdFolder.getUser()).isNotNull();
    assertThat(createdFolder.getPaths()).isEqualTo(
      Collections.singletonList(returnedFolder));

    verify(userService).getUser(userId);
    verify(fileRepository).findByIdAndDeletedFalse(PARENT_ID);
    verify(fileRepository).findByIdAndDeletedFalse(returnedFolder.getId());
    verify(fileRepository, times(2)).save(any(FileV2.class));
    verifyZeroInteractions(resourceService, fileProperties);
  }

  @Test
  public void testGivenMethodCallAndEmptyByteArrayAndInvalidRoleByCreatingFolderReturnForbiddenException() {

    catchException(
      () -> fileService.createFileOrFolder(new Session("", "", "", EMAIL, Role.MENTOR),
                                           PARENT_ID, NAME, NAME, new byte[] {}
      ));

    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Invalid Role For Creating Folder");

    verifyZeroInteractions(
      fileRepository, resourceService, fileProperties, userService);
  }

  @Test
  public void testGivenMethodCallAndNonEmptyByteArrayByUpdatingFileOrFolderReturnUpdatedFile() {

    when(fileRepository.findByIdAndParentIdAndDeletedFalse(ID,
                                                           PARENT_ID
    )).thenReturn(Optional.of(file));
    when(resourceService.storeFile(ID, PARENT_ID, NAME, NAME, NAME.getBytes(),
                                   FileOrigin.FILE
    )).thenReturn(file);
    when(fileRepository.findOne(ID)).thenReturn(file);
    when(fileRepository.save(file)).thenReturn(file);

    FileV2 updatedFile = fileService.updateFileOrFolder(
      SESSION, ID, PARENT_ID, NAME, NAME, NAME.getBytes());

    assertThat(updatedFile).isNotNull();
    assertThat(updatedFile).isEqualTo(file);

    verify(fileRepository).findByIdAndParentIdAndDeletedFalse(ID, PARENT_ID);
    verify(resourceService).storeFile(
      ID, PARENT_ID, NAME, NAME, NAME.getBytes(), FileOrigin.FILE);
    verify(fileRepository).findOne(ID);
    verify(fileRepository).save(file);
    verifyZeroInteractions(fileProperties, userService);
  }

  @Test
  public void testGivenMethodCallAndEmptyByteArrayByUpdatingFileOrFolderReturnUpdatedFolder() {

    FileV2 folder = FileV2.builder()
      .name(NAME)
      .parentId(PARENT_ID)
      .used(true)
      .markFolder(true)
      .build();

    when(fileRepository.findByIdAndParentIdAndDeletedFalse(folder.getId(),
                                                           PARENT_ID
    )).thenReturn(Optional.of(folder));
    when(fileRepository.save(folder)).thenReturn(folder);

    FileV2 updatedFolder = fileService.updateFileOrFolder(
      SESSION, folder.getId(), PARENT_ID, NAME, NAME, new byte[] {});

    assertThat(updatedFolder).isNotNull();
    assertThat(updatedFolder).isEqualTo(folder);

    verify(fileRepository).findByIdAndParentIdAndDeletedFalse(
      folder.getId(), PARENT_ID);
    verify(fileRepository).save(folder);
    verifyZeroInteractions(resourceService, fileProperties, userService);
  }

}
