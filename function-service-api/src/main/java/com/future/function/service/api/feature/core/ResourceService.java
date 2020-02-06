package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.FileV2;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ResourceService {

  FileV2 createACopy(FileV2 sourceFile, FileOrigin fileOrigin);

  FileV2 storeAndSaveFile(
    String objectName, String fileName, byte[] bytes, FileOrigin fileOrigin
  );

  FileV2 storeFile(
    String fileId, String parentId, String objectName, String fileName,
    byte[] bytes, FileOrigin fileOrigin
  );

  FileV2 getFile(String fileId);

  boolean markFilesUsed(List<String> fileIds, boolean used);

  Pair<String, byte[]> getFileAsByteArray(
    Role role, String fileName, FileOrigin fileOrigin, Long version
  );

}
