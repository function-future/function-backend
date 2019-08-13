package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.session.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface FileService {
  
  FileV2 getFileOrFolder(String fileFolderId, String parentId);
  
  Pair<List<FileV2>, Page<FileV2>> getFilesAndFolders(
    String parentId, Pageable pageable
  );
  
  FileV2 createFileOrFolder(
    Session session, String parentId, String objectName, String fileName,
    byte[] bytes
  );
  
  FileV2 updateFileOrFolder(
    Session session, String fileOrFolderId, String parentId, String objectName,
    String fileName, byte[] bytes
  );
  
  void deleteFileOrFolder(
    Session session, String parentId, String fileFolderId
  );
  
}
