package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.impl.helper.FileHelper;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

  private static final String URL_SEPARATOR = "/";

  private final FileRepositoryV2 fileRepositoryV2;

  private final List<String> imageExtensions;

  private final String storagePath;

  private final String thumbnailSuffix;

  public ResourceServiceImpl(
    FileRepositoryV2 fileRepositoryV2, FileProperties fileProperties
  ) {

    this.fileRepositoryV2 = fileRepositoryV2;

    imageExtensions = fileProperties.getImageExtensions();
    storagePath = fileProperties.getStoragePath() + FileHelper.PATH_SEPARATOR;
    thumbnailSuffix = fileProperties.getThumbnailSuffix();
  }

  @Override
  public FileV2 createACopy(FileV2 sourceFile, FileOrigin fileOrigin) {

    FileV2 file = this.storeAndSaveFile(sourceFile.getName(),
                                        sourceFile.getFileUrl(),
                                        FileHelper.toByteArray(
                                          new File(sourceFile.getFilePath())),
                                        fileOrigin
    );
    this.markFilesUsed(Collections.singletonList(file.getId()), true);

    return file;
  }

  @Override
  public FileV2 storeAndSaveFile(
    String objectName, String fileName, byte[] bytes, FileOrigin fileOrigin
  ) {

    return fileRepositoryV2.save(
      this.storeFile(null, null, objectName, fileName, bytes, fileOrigin));
  }

  @Override
  public FileV2 storeFile(
    String fileId, String parentId, String objectName, String fileName,
    byte[] bytes, FileOrigin fileOrigin
  ) {

    FileV2 fileV2 = this.buildFileV2(fileId, parentId, objectName);

    String extension = FilenameUtils.getExtension(fileName);

    if (imageExtensions.contains(FileHelper.DOT + extension.toLowerCase())) {
      createThumbnail(bytes, fileV2, extension, fileOrigin);
    }

    createFile(bytes, fileV2, extension, fileOrigin);

    fileV2.setAsResource(fileOrigin.isAsResource());

    return fileV2;
  }

  private FileV2 buildFileV2(
    String fileId, String parentId, String objectName
  ) {

    FileV2 fileV2 = Optional.ofNullable(fileId)
      .map(id -> fileRepositoryV2.findOne(fileId))
      .orElseGet(() -> FileV2.builder()
        .build());

    if (!StringUtils.isEmpty(fileId)) {
      fileV2.setId(fileId);
    }

    if (!StringUtils.isEmpty(parentId)) {
      fileV2.setParentId(parentId);
    }

    fileV2.setName(objectName);

    return fileV2;
  }

  @Override
  public FileV2 getFile(String fileId) {

    return fileRepositoryV2.findOne(fileId);
  }

  @Override
  public boolean markFilesUsed(List<String> fileIds, boolean used) {

    return this.getFileV2List(fileIds)
      .stream()
      .map(fileV2 -> {
        fileV2.setUsed(used);
        return fileRepositoryV2.save(fileV2);
      })
      .allMatch(Objects::nonNull);
  }

  private ArrayList<FileV2> getFileV2List(List<String> fileIds) {

    return Optional.of(fileIds)
      .map(fileRepositoryV2::findAll)
      .map(Lists::newArrayList)
      .filter(list -> list.size() == fileIds.size())
      .orElseThrow(() -> new NotFoundException("File Not Found"));
  }

  @Override
  public Pair<String, byte[]> getFileAsByteArray(
    Role role, String fileName, FileOrigin fileOrigin, Long version
  ) {

    return Optional.of(fileOrigin)
      .map(origin -> checkIsValidForGettingData(role, origin))
      .flatMap(origin -> fileRepositoryV2.findByIdAndAsResource(
        this.getFileId(fileName), origin.isAsResource()))
      .map(file -> Pair.of(
        this.getDownloadName(file, version),
        this.getFileOrThumbnail(file, fileName, version)
      ))
      .map(pair -> Pair.of(pair.getFirst(),
                           FileHelper.toByteArray(pair.getSecond())
      ))
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }

  private String getDownloadName(FileV2 file, Long version) {

    Pair<String, String> downloadFileNameAndExtension =
      this.getDownloadFileNameAndExtension(
        version, this.getFileNameAndExtension(file));

    return this.appendExtensionToFileName(downloadFileNameAndExtension);
  }

  private Pair<String, String> getDownloadFileNameAndExtension(
    Long version, Pair<String, String> originalFileNameAndExtension
  ) {

    return Optional.ofNullable(version)
      .map(ver -> Pair.of(
        originalFileNameAndExtension.getFirst()
          .concat("-v" + String.valueOf(ver)),
        originalFileNameAndExtension.getSecond()
      ))
      .orElse(originalFileNameAndExtension);
  }

  private String appendExtensionToFileName(
    Pair<String, String> fileNameAndExtension
  ) {

    return fileNameAndExtension.getFirst()
      .concat(".")
      .concat(fileNameAndExtension.getSecond());
  }

  private Pair<String, String> getFileNameAndExtension(FileV2 fileObject) {

    String baseNameExtractionTarget = Optional.of(fileObject)
      .map(FileV2::getName)
      .orElse(fileObject.getFilePath());

    return Pair.of(
      FilenameUtils.getBaseName(baseNameExtractionTarget),
      FilenameUtils.getExtension(fileObject.getFilePath())
    );
  }

  private FileOrigin checkIsValidForGettingData(
    Role role, FileOrigin fileOrigin
  ) {

    if (role.equals(Role.UNKNOWN) && this.isFileOriginNotAccessibleByGuest(
      fileOrigin)) {
      throw new UnauthorizedException("Invalid Session for Guest");
    }

    return fileOrigin;
  }

  private boolean isFileOriginNotAccessibleByGuest(FileOrigin fileOrigin) {

    boolean isAnnouncement = fileOrigin.equals(FileOrigin.ANNOUNCEMENT);
    boolean isActivityBlog = fileOrigin.equals(FileOrigin.BLOG);

    return !(isAnnouncement || isActivityBlog);
  }

  private File getFileOrThumbnail(FileV2 file, String fileName, Long version) {

    return Optional.ofNullable(version)
      .map(v -> this.getFileByVersion(file, v))
      .orElseGet(() -> this.getFileOrThumbnail(file, fileName));
  }

  private File getFileByVersion(FileV2 file, Long version) {

    return Optional.ofNullable(version)
      .map(result -> new File(file.getVersions()
                                .get(version)
                                .getPath()))
      .orElseGet(() -> new File(file.getFilePath()));
  }

  private File getFileOrThumbnail(FileV2 file, String fileName) {

    return Optional.of(fileName)
      .filter(FileHelper::isThumbnailName)
      .map(result -> new File(file.getThumbnailPath()))
      .orElseGet(() -> new File(file.getFilePath()));
  }

  private String getFileId(String fileName) {

    return fileName.substring(0, 36);
  }

  private void createFile(
    byte[] bytes, FileV2 fileV2, String extension, FileOrigin fileOrigin
  ) {

    String filePath = constructPathOrUrl(
      storagePath + fileOrigin.lowCaseValue() + FileHelper.PATH_SEPARATOR +
      fileV2.getId() + this.getFileVersion(fileV2) + FileHelper.PATH_SEPARATOR,
      fileV2.getId(), extension
    );
    FileHelper.createJavaIoFile(bytes, filePath);

    fileV2.setFilePath(filePath);
    fileV2.setFileUrl(constructPathOrUrl(URL_SEPARATOR + fileOrigin.name()
      .toLowerCase() + URL_SEPARATOR, fileV2.getId(), extension));
  }

  private void createThumbnail(
    byte[] bytes, FileV2 fileV2, String extension, FileOrigin fileOrigin
  ) {

    String thumbnailName = fileV2.getId() + thumbnailSuffix;
    String thumbnailPath = constructPathOrUrl(
      storagePath + fileOrigin.lowCaseValue() + FileHelper.PATH_SEPARATOR +
      fileV2.getId() + this.getFileVersion(fileV2) + FileHelper.PATH_SEPARATOR,
      thumbnailName, extension
    );
    FileHelper.createThumbnail(bytes, thumbnailPath, extension);

    fileV2.setThumbnailPath(thumbnailPath);
    fileV2.setThumbnailUrl(constructPathOrUrl(URL_SEPARATOR + fileOrigin.name()
      .toLowerCase() + URL_SEPARATOR, thumbnailName, extension));
  }

  private String getFileVersion(FileV2 fileV2) {

    return Optional.ofNullable(fileV2)
      .map(FileV2::getVersion)
      .map(version -> "-" + (version + 1))
      .orElse("-0");
  }

  private String constructPathOrUrl(
    String pathOrUrl, String name, String extension
  ) {

    return pathOrUrl + name + FileHelper.DOT + extension;
  }

}
