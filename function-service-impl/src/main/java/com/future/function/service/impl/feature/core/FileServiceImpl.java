package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  
  private FileV2 copyPropertiesAndSaveFileV2(Pair<FileV2, FileV2> pair) {
    
    CopyHelper.copyProperties(pair.getFirst(), pair.getSecond());
    return fileRepositoryV2.save(pair.getSecond());
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
      .flatMap(id -> fileRepositoryV2.findByIdAndParentId(id, parentId))
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
      .map(id -> fileRepositoryV2.findAllByParentId(id, pageable))
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
   * @param email          Email of current user.
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
    String email, String fileOrFolderId, String parentId, String objectName,
    String fileName, byte[] bytes
  ) {
    
    FileV2 fileV2 = this.getFileOrFolder(fileOrFolderId, parentId);
    
    return Optional.of(fileV2)
      .filter(file -> !file.isMarkFolder())
      .map(file -> this.updateFile(file, email, fileOrFolderId, parentId,
                                   objectName, fileName, bytes
      ))
      .orElseGet(() -> this.updateFolder(email, fileV2));
  }
  
  private FileV2 updateFolder(String email, FileV2 fileV2) {
    
    return Optional.of(fileV2)
      //      .filter(file -> email.equals(file.getCreatedBy()))
      .map(fileRepositoryV2::save)
      .orElse(fileV2);
  }
  
  private FileV2 updateFile(
    FileV2 fileV2, String email, String fileOrFolderId, String parentId,
    String objectName, String fileName, byte[] bytes
  ) {
    
    return Optional.of(fileV2)
      //      .filter(file -> email.equals(file.getCreatedBy()))
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
  
  
  /**
   * {@inheritDoc}
   *
   * @param email        Email of current user.
   * @param fileFolderId Id of file/folder to be deleted.
   */
  @Override
  public void deleteFileOrFolder(
    String email, String parentId, String fileFolderId
  ) {
    
    Optional.ofNullable(fileFolderId)
      .filter(id -> !id.equalsIgnoreCase(fileProperties.getRootId()))
      .flatMap(id -> fileRepositoryV2.findByIdAndParentId(id, parentId))
      //      .filter(file -> email.equalsIgnoreCase(file.getCreatedBy()))
      .ifPresent(
        file -> resourceService.markFilesUsed(this.getListOfIdToBeMarked(file),
                                              false
        ));
  }
  
  private List<String> getListOfIdToBeMarked(FileV2 file) {
    
    List<String> fileIds = fileRepositoryV2.findAllByParentId(file.getId())
      .map(FileV2::getId)
      .collect(Collectors.toList());
    
    fileIds.add(file.getId());
    
    return fileIds;
  }
  
}
