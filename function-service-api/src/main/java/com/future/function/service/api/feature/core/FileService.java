package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.model.entity.feature.core.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  
  File getFile(String id);
  
  byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin);
  
  File storeFile(MultipartFile multipartFile, FileOrigin fileOrigin);
  
  void deleteFile(String id);
  
}
