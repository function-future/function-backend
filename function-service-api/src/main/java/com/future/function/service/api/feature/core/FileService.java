package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.model.entity.feature.core.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for file logic operations declaration.
 */
public interface FileService {
  
  /**
   * Retrieves a file from database given the file's id. If not found,
   * then throw {@link com.future.function.common.exception.NotFoundException}
   * exception.
   *
   * @param id Id of file to be retrieved.
   *
   * @return {@code File} - The file object found in database.
   */
  File getFile(String id);
  
  /**
   * Retrieves a file as byte array to be consumed/used by end-users. If not
   * found, then throw
   * {@link com.future.function.common.exception.NotFoundException}
   * exception.
   *
   * @param fileName   Name of the file to be obtained.
   * @param fileOrigin Origin of the file, which in this case specifies at
   *                   which folder does the file reside in the file storage.
   *
   * @return {@code byte[]} - The byte array of the file.
   */
  byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin);
  
  /**
   * Saves a file to database and storage. If the request is not
   * acceptable/processable, then throw
   * {@link com.future.function.common.exception.BadRequestException}
   * exception.
   *
   * @param multipartFile The original file sent from client side.
   * @param fileOrigin    Origin of the file, which in this case specifies at
   *                      which folder the file will reside in the file
   *                      storage.
   *
   * @return {@code File} - The file object of the saved data.
   */
  File storeFile(MultipartFile multipartFile, FileOrigin fileOrigin);
  
  /**
   * Deletes a file from database and storage. If not found,
   * then throw {@link com.future.function.common.exception.NotFoundException}
   * exception.
   *
   * @param id Id of the file to be deleted.
   */
  void deleteFile(String id);
  
}
