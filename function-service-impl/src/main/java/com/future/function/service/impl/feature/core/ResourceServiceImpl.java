package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.impl.helper.FileHelper;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {
  
  private static final String URL_SEPARATOR = "/";
  
  private final FileRepositoryV2 fileRepositoryV2;
  
  private final List<String> imageExtensions;
  
  private final String urlPrefix;
  
  private final String storagePath;
  
  private final String thumbnailSuffix;
  
  public ResourceServiceImpl(
    FileRepositoryV2 fileRepositoryV2, FileProperties fileProperties
  ) {
    
    this.fileRepositoryV2 = fileRepositoryV2;
    
    imageExtensions = fileProperties.getImageExtensions();
    urlPrefix = fileProperties.getUrlPrefix() + URL_SEPARATOR;
    storagePath = fileProperties.getStoragePath() + FileHelper.PATH_SEPARATOR;
    thumbnailSuffix = fileProperties.getThumbnailSuffix();
  }
  
  @Override
  public FileV2 storeFile(
    String objectName, String fileName, byte[] bytes, FileOrigin fileOrigin
  ) {
    
    FileV2 fileV2 = FileV2.builder()
      .name(objectName)
      .build();
    
    String extension = FilenameUtils.getExtension(fileName);
    
    if (imageExtensions.contains(FileHelper.DOT + extension)) {
      createThumbnail(bytes, fileV2, extension, fileOrigin);
    }
    
    createFile(bytes, fileV2, extension, fileOrigin);
    
    fileV2.setAsResource(fileOrigin.isAsResource());
    
    return fileRepositoryV2.save(fileV2);
  }
  
  @Override
  public boolean markFilesUsed(List<String> fileIds, boolean used) {
    
    Optional.of(fileIds)
      .map(fileRepositoryV2::findAll)
      .map(Lists::newArrayList)
      .filter(list -> list.size() == fileIds.size())
      .orElseThrow(() -> new NotFoundException("File Not Found"))
      .forEach(fileV2 -> fileV2.setUsed(used));
    
    return true;
  }
  
  @Override
  public byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin) {
    
    return fileRepositoryV2.findByIdAndAsResource(
      this.getFileId(fileName), fileOrigin.isAsResource())
      .map(file -> this.getFileOrThumbnail(file, fileName))
      .map(FileHelper::toByteArray)
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }
  
  private File getFileOrThumbnail(FileV2 file, String fileName) {
    
    return Optional.of(fileName)
      .filter(FileHelper::isThumbnailName)
      .map(result -> new java.io.File(file.getThumbnailPath()))
      .orElseGet(() -> new java.io.File(file.getFilePath()));
  }
  
  private String getFileId(String fileName) {
    
    return fileName.substring(0, 36);
  }
  
  private void createFile(
    byte[] bytes, FileV2 fileV2, String extension, FileOrigin fileOrigin
  ) {
    
    String filePath = constructPathOrUrl(
      storagePath + fileOrigin.lowCaseValue() + FileHelper.PATH_SEPARATOR +
      fileV2.getId() + FileHelper.PATH_SEPARATOR, fileV2.getId(), extension);
    FileHelper.createJavaIoFile(bytes, filePath);
    
    fileV2.setFilePath(filePath);
    fileV2.setFileUrl(constructPathOrUrl(urlPrefix + fileOrigin.name()
      .toLowerCase() + URL_SEPARATOR, fileV2.getId(), extension));
  }
  
  private void createThumbnail(
    byte[] bytes, FileV2 fileV2, String extension, FileOrigin fileOrigin
  ) {
    
    String thumbnailName = fileV2.getId() + thumbnailSuffix;
    String thumbnailPath = constructPathOrUrl(
      storagePath + fileOrigin.lowCaseValue() + FileHelper.PATH_SEPARATOR +
      fileV2.getId() + FileHelper.PATH_SEPARATOR, thumbnailName, extension);
    FileHelper.createThumbnail(bytes, thumbnailPath, extension);
    
    fileV2.setThumbnailPath(thumbnailPath);
    fileV2.setThumbnailUrl(constructPathOrUrl(urlPrefix + fileOrigin.name()
      .toLowerCase() + URL_SEPARATOR, thumbnailName, extension));
  }
  
  private String constructPathOrUrl(
    String pathOrUrl, String name, String extension
  ) {
    
    return pathOrUrl + name + FileHelper.DOT + extension;
  }
  
}
