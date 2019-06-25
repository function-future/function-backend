package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {
  
  private final FileRepositoryV2 fileRepositoryV2;
  
  private final ResourceService resourceService;
  
  private final FileProperties fileProperties;
  
  @Autowired
  public FileServiceImpl(
    FileRepositoryV2 fileRepositoryV2, ResourceService resourceService,
    FileProperties fileProperties
  ) {
    
    this.fileRepositoryV2 = fileRepositoryV2;
    this.resourceService = resourceService;
    this.fileProperties = fileProperties;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param fileFolderId Id of file/folder to be retrieved.
   * @param parentId     Id of parent of file/folder to be retrieved.
   *
   * @return {@code FileV2} - The file/folder object found in database.
   */
  @Override
  public FileV2 getFileOrFolder(String fileFolderId, String parentId) {
    
    return Optional.ofNullable(fileFolderId)
      .flatMap(id -> fileRepositoryV2.findByIdAndParentIdAndDeletedFalse(id, parentId))
      .orElseThrow(() -> new NotFoundException("Get File/Folder Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param parentId Id of parent of files/folders to be retrieved.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<FileV2>} - Page of files/folders found in
   * database.
   */
  @Override
  public Page<FileV2> getFilesAndFolders(String parentId, Pageable pageable) {
    
    return Optional.ofNullable(parentId)
      .map(
        id -> fileRepositoryV2.findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDesc(
          id, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param objectName Name of file, to be stored as object's name.
   * @param fileName   Original name of file.
   * @param bytes      Byte array of file content.
   *
   * @return {@code FileV2} - The file/folder object of the saved data.
   */
  @Override
  public FileV2 createFileOrFolder(
    String parentId, String objectName, String fileName, byte[] bytes
  ) {
    
    return Optional.of(bytes)
      .filter(b -> b.length != 0)
      .map(b -> this.createAndSaveFile(parentId, objectName, fileName, b))
      .orElseGet(() -> this.createAndSaveFolder(parentId, objectName));
  }
  
  private FileV2 createAndSaveFolder(String parentId, String objectName) {
    
    return fileRepositoryV2.save(this.buildFolder(parentId, objectName));
  }
  
  private FileV2 createAndSaveFile(
    String parentId, String objectName, String fileName, byte[] bytes
  ) {
    
    FileV2 file = resourceService.storeFile(null, parentId, objectName,
                                            fileName, bytes, FileOrigin.FILE
    );
    file.setUsed(true);
    
    return fileRepositoryV2.save(file);
  }
  
  private FileV2 buildFolder(String parentId, String objectName) {
    
    return FileV2.builder()
      .name(objectName)
      .parentId(parentId)
      .used(true)
      .markFolder(true)
      .build();
  }
  
  /**
   * {@inheritDoc}
   *
   * @param session        Current user's session.
   * @param fileOrFolderId Id of file/folder to-be-updated.
   * @param parentId       Id of parent of file/folder.
   * @param objectName     Name of file, to be stored as object's name.
   * @param fileName       Original name of file.
   * @param bytes          Byte array of file content.
   *
   * @return {@code FileV2} - The file/folder object of the saved data.
   */
  @Override
  public FileV2 updateFileOrFolder(
    Session session, String fileOrFolderId, String parentId, String objectName,
    String fileName, byte[] bytes
  ) {
    
    FileV2 fileV2 = this.getFileOrFolder(fileOrFolderId, parentId);
    
    return Optional.of(fileV2)
      .filter(file -> !file.isMarkFolder())
      .map(file -> this.updateFile(file, session, fileOrFolderId, parentId,
                                   objectName, fileName, bytes
      ))
      .orElseGet(() -> this.updateFolder(session, fileV2, objectName));
  }
  
  private FileV2 updateFolder(Session session, FileV2 fileV2, String name) {
    
    fileV2.setName(name);
    return Optional.of(fileV2)
      .filter(
        file -> AuthorizationHelper.isAuthorizedForEdit(session.getEmail(),
                                                        session.getRole(), file,
                                                        Role.ADMIN
        ))
      .map(fileRepositoryV2::save)
      .orElse(fileV2);
  }
  
  private FileV2 updateFile(
    FileV2 fileV2, Session session, String fileOrFolderId, String parentId,
    String objectName, String fileName, byte[] bytes
  ) {
    
    return Optional.of(fileV2)
      .filter(
        file -> AuthorizationHelper.isAuthorizedForEdit(session.getEmail(),
                                                        session.getRole(), file,
                                                        Role.ADMIN
        ))
      .map(ignored -> resourceService.storeFile(fileOrFolderId, parentId,
                                                objectName, fileName, bytes,
                                                FileOrigin.FILE
      ))
      .map(storedFile -> Pair.of(storedFile,
                                 fileRepositoryV2.findOne(fileV2.getId())
      ))
      .map(this::copyPropertiesAndSaveFileV2)
      .orElse(fileV2);
  }
  
  private FileV2 copyPropertiesAndSaveFileV2(Pair<FileV2, FileV2> pair) {
    
    CopyHelper.copyProperties(pair.getFirst(), pair.getSecond());
    return fileRepositoryV2.save(pair.getSecond());
  }
  
  /**
   * {@inheritDoc}
   *
   * @param session      Current user's session.
   * @param parentId     Id of parent of file/folder.
   * @param fileFolderId Id of file/folder to be deleted.
   */
  @Override
  public void deleteFileOrFolder(
    Session session, String parentId, String fileFolderId
  ) {
    
    Optional.ofNullable(fileFolderId)
      .filter(id -> !id.equalsIgnoreCase(fileProperties.getRootId()))
      .flatMap(id -> fileRepositoryV2.findByIdAndParentIdAndDeletedFalse(id, parentId))
      .filter(
        file -> AuthorizationHelper.isAuthorizedForEdit(session.getEmail(),
                                                        session.getRole(), file,
                                                        Role.ADMIN
        ))
      .ifPresent(file -> {
        List<String> fileIds = this.getListOfIdToBeMarked(file);
        
        resourceService.markFilesUsed(fileIds, false);
        
        // Prevents optimistic locking
        Iterable<FileV2> files = fileRepositoryV2.findAll(fileIds);
        
        files.forEach(fileV2 -> fileV2.setDeleted(true));
        fileRepositoryV2.save(files);
      });
  }
  
  private List<String> getListOfIdToBeMarked(FileV2 file) {
    
    Set<String> fileIds = new HashSet<>();
  
    String fileId = file.getId();
  
    List<FileV2> filesWithFileAsParent = fileRepositoryV2.findAllByParentIdAndDeletedFalse(
      fileId);
    if (!filesWithFileAsParent.isEmpty()) {
      List<String> ids = filesWithFileAsParent
        .stream()
        .map(this::getListOfIdToBeMarked)
        .reduce(new ArrayList<>(), (collectedFileIds, retrievedFileIds) -> {
          collectedFileIds.addAll(retrievedFileIds);
          return collectedFileIds;
        });
    
      fileIds.addAll(ids);
    }
  
    fileIds.add(fileId);
  
    return new ArrayList<>(fileIds);
  }
  
}
