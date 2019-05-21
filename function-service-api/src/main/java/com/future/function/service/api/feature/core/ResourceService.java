package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.model.entity.feature.core.FileV2;

import java.util.List;

public interface ResourceService {
  
  FileV2 storeFile(String objectName, String fileName, byte[] bytes,
                   FileOrigin fileOrigin);
  
  FileV2 getFile(String fileId);
  
  boolean markFilesUsed(List<String> fileIds, boolean used);
  
  byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin);
  
}
