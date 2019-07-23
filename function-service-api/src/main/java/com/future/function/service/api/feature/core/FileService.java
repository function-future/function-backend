package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.session.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * Service interface class for file/folder logic operations declaration.
 */
public interface FileService {
  
  /**
   * Retrieves a file/folder from database given the file/folder's id. If
   * not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param fileFolderId Id of file/folder to be retrieved.
   * @param parentId     Id of parent of file/folder to be retrieved.
   *
   * @return {@code FileV2} - The file/folder object found in database.
   */
  FileV2 getFileOrFolder(String fileFolderId, String parentId);
  
  /**
   * Retrieves files/folders from database.
   *
   * @param parentId Id of parent of files/folders to be retrieved.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Pair<List<FileV2>, Page<FileV2>>} - Pair of
   * paths and files/folders found in database.
   */
  Pair<List<FileV2>, Page<FileV2>> getFilesAndFolders(
    String parentId, Pageable pageable
  );
  
  /**
   * Creates file/folder object and saves any other data related to the
   * file/folder.
   *
   * @param session        Current user's session.
   * @param parentId   Id of parent of file/folder.
   * @param objectName Name of file, to be stored as object's name.
   * @param fileName   Original name of file.
   * @param bytes      Byte array of file content.
   *
   * @return {@code FileV2} - The file/folder object of the saved data.
   */
  FileV2 createFileOrFolder(
    Session session, String parentId, String objectName, String fileName,
    byte[] bytes
  );
  
  /**
   * Updates file/folder object and saves any other data related to the
   * file/folder.
   *
   * @param session        Current user's session.
   * @param fileOrFolderId Id of file/folder to-be-updated.
   * @param parentId       Id of parent of file/folder.
   * @param objectName     Name of file, to be stored as object's name.
   * @param fileName       Original name of file.
   * @param bytes          Byte array of file content.
   *
   * @return {@code FileV2} - The file/folder object of the saved data.
   */
  FileV2 updateFileOrFolder(
    Session session, String fileOrFolderId, String parentId, String objectName,
    String fileName, byte[] bytes
  );
  
  /**
   * Deletes file/folder object from database.
   *
   * @param session      Current user's session.
   * @param parentId     Id of parent of file/folder.
   * @param fileFolderId Id of file/folder to be deleted.
   */
  void deleteFileOrFolder(
    Session session, String parentId, String fileFolderId
  );
  
}
