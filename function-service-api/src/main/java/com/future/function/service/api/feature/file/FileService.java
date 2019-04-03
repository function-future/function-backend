package com.future.function.service.api.feature.file;

import com.future.function.common.enumeration.FileOrigin;
import com.future.function.model.entity.feature.file.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  
  File getFile(String id, FileOrigin fileOrigin);
  
  File storeFile(MultipartFile multipartFile, FileOrigin origin);
  
  void deleteFile(String id);
  
}
