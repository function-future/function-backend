package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.FilePath;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import com.future.function.session.model.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

  private final FileRepositoryV2 fileRepositoryV2;

  private final ResourceService resourceService;

  private final FileProperties fileProperties;

  private final UserService userService;

  @Autowired
  public FileServiceImpl(
    FileRepositoryV2 fileRepositoryV2, ResourceService resourceService,
    FileProperties fileProperties, UserService userService
  ) {

    this.fileRepositoryV2 = fileRepositoryV2;
    this.resourceService = resourceService;
    this.fileProperties = fileProperties;
    this.userService = userService;
  }

  private FileV2 createAndSaveFolder(
    Session session, String parentId, String objectName
  ) {

    return Optional.of(session)
      .filter(sess -> AuthorizationHelper.isRoleValidForEdit(sess.getRole(),
                                                             Role.ADMIN
      ))
      .map(sess -> this.buildFolder(parentId, objectName, sess.getUserId()))
      .map(fileRepositoryV2::save)
      .map(file -> {
        file.setPaths(this.getPathsForFileOrFolder(file.getId()));
        return file;
      })
      .map(fileRepositoryV2::save)
      .orElseThrow(
        () -> new ForbiddenException("Invalid Role For Creating Folder"));
  }

  private FileV2 createAndSaveFile(
    Session session, String parentId, String objectName, String fileName,
    byte[] bytes
  ) {

    FileV2 file = resourceService.storeFile(null, parentId, objectName,
                                            fileName, bytes, FileOrigin.FILE
    );
    file.setUsed(true);
    file.setUser(userService.getUser(session.getUserId()));

    file = fileRepositoryV2.save(file);

    file.setPaths(this.getPathsForFileOrFolder(file.getId()));

    return fileRepositoryV2.save(file);
  }

  private List<FilePath> getPathsForFileOrFolder(FileV2 fileOrFolder) {

    if (Objects.isNull(fileOrFolder)) {
      return new LinkedList<>();
    }

    String parentId = Optional.ofNullable(fileOrFolder.getParentId())
      .orElse(null);

    return this.getPathsForFileOrFolder(parentId);
  }

  private FileV2 buildFolder(
    String parentId, String objectName, String userId
  ) {

    return FileV2.builder()
      .name(objectName)
      .parentId(parentId)
      .used(true)
      .markFolder(true)
      .user(userService.getUser(userId))
      .build();
  }

  private List<FilePath> getPathsForFileOrFolder(String fileFolderId) {

    FileV2 fileOrFolder = Optional.ofNullable(fileFolderId)
      .flatMap(fileRepositoryV2::findByIdAndDeletedFalse)
      .orElse(null);

    List<FilePath> pathsForFileOrFolder = this.getPathsForFileOrFolder(
      fileOrFolder);

    Optional.ofNullable(fileOrFolder)
      .map(this::toFilePath)
      .ifPresent(pathsForFileOrFolder::add);

    return pathsForFileOrFolder;
  }

  private FilePath toFilePath(FileV2 fileOrFolder) {

    return FilePath.builder()
      .id(fileOrFolder.getId())
      .name(fileOrFolder.getName())
      .build();
  }

  private List<String> getListOfIdToBeMarked(FileV2 file) {

    Set<String> fileIds = new HashSet<>();

    String fileId = file.getId();

    List<FileV2> filesWithFileAsParent =
      fileRepositoryV2.findAllByParentIdAndDeletedFalse(fileId);
    if (!filesWithFileAsParent.isEmpty()) {
      List<String> ids = filesWithFileAsParent.stream()
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

  @Override
  public FileV2 getFileOrFolder(String fileFolderId, String parentId) {

    return Optional.ofNullable(fileFolderId)
      .flatMap(
        id -> fileRepositoryV2.findByIdAndParentIdAndDeletedFalse(id, parentId))
      .orElseThrow(() -> new NotFoundException("Get File/Folder Not Found"));
  }

  @Override
  public Pair<List<FilePath>, Page<FileV2>> getFilesAndFolders(
    String parentId, Pageable pageable
  ) {

    List<FilePath> paths = this.getPathsForFileOrFolder(parentId);

    return Optional.ofNullable(parentId)
      .map(
        id -> fileRepositoryV2.findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDescNameAsc(
          id, pageable))
      .map(data -> Pair.of(paths, data))
      .orElseGet(() -> Pair.of(paths, PageHelper.empty(pageable)));
  }

  @Override
  public FileV2 createFileOrFolder(
    Session session, String parentId, String objectName, String fileName,
    byte[] bytes
  ) {

    return Optional.of(bytes)
      .filter(b -> b.length != 0)
      .map(
        b -> this.createAndSaveFile(session, parentId, objectName, fileName, b))
      .orElseGet(() -> this.createAndSaveFolder(session, parentId, objectName));
  }

  @Override
  public void deleteFileOrFolder(
    Session session, String parentId, String fileFolderId
  ) {

    Optional.ofNullable(fileFolderId)
      .filter(id -> !id.equalsIgnoreCase(fileProperties.getRootId()))
      .flatMap(
        id -> fileRepositoryV2.findByIdAndParentIdAndDeletedFalse(id, parentId))
      .filter(
        file -> AuthorizationHelper.isAuthorizedForEdit(session.getUserId(),
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
        file -> AuthorizationHelper.isAuthorizedForEdit(session.getUserId(),
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
        file -> AuthorizationHelper.isAuthorizedForEdit(session.getUserId(),
                                                        session.getRole(), file,
                                                        Role.ADMIN
        ))
      .map(
        ignored -> storeOrCreateSourceFile(fileOrFolderId, parentId, objectName,
                                           fileName, bytes,
                                           fileRepositoryV2.findOne(
                                             fileV2.getId())
        ))
      .map(this::copyPropertiesAndSaveFileV2)
      .orElse(fileV2);
  }

  private FileV2 copyPropertiesAndSaveFileV2(Pair<FileV2, FileV2> pair) {

    CopyHelper.copyProperties(pair.getFirst(), pair.getSecond());
    return fileRepositoryV2.save(pair.getSecond());
  }

  private Pair<FileV2, FileV2> storeOrCreateSourceFile(
    String fileOrFolderId, String parentId, String objectName, String fileName,
    byte[] bytes, FileV2 foundFileFolder
  ) {

    if (bytes == null || bytes.length == 0) {
      FileV2 returnedFile = new FileV2();
      BeanUtils.copyProperties(foundFileFolder, returnedFile);
      returnedFile.setName(objectName);
      return Pair.of(returnedFile, foundFileFolder);
    }

    return Pair.of(
      resourceService.storeFile(fileOrFolderId, parentId, objectName, fileName,
                                bytes, FileOrigin.FILE
      ), foundFileFolder);
  }

}
