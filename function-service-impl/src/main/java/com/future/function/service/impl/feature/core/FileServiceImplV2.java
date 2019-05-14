package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.service.api.feature.core.FileServiceV2;
import com.future.function.service.impl.helper.FileHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileServiceImplV2 implements FileServiceV2 {
  
  private final FileRepositoryV2 fileRepositoryV2;
  
  public FileServiceImplV2(FileRepositoryV2 fileRepositoryV2) {
    
    this.fileRepositoryV2 = fileRepositoryV2;
  }
  
  @Override
  public byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin) {
    
    return fileRepositoryV2.findByIdAndAsResource(
      this.getFileId(fileName), fileOrigin.isAsResource())
      .map(file -> getFileOrThumbnail(file, fileName))
      .map(FileHelper::toByteArray)
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }
  
  private java.io.File getFileOrThumbnail(FileV2 file, String fileName) {
    
    return Optional.of(fileName)
      .filter(FileHelper::isThumbnailName)
      .map(result -> new java.io.File(file.getThumbnailPath()))
      .orElseGet(() -> new java.io.File(file.getFilePath()));
  }
  
  private String getFileId(String fileName) {
    
    return fileName.substring(0, 36);
  }
  
}
