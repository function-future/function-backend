package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;

public interface FileServiceV2 {
  
  byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin);
  
}
