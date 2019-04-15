package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.service.api.feature.core.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for file APIs
 */
@RestController
@RequestMapping(value = "/files")
public class FileController {
  
  private final FileService fileService;
  
  @Autowired
  public FileController(FileService fileService) {
    
    this.fileService = fileService;
  }
  
  /**
   * Retrieves byte array data of the specified file from given parameters.
   *
   * @param origin   Origin of file to be retrieved in file storage.
   * @param fileName Name of file to be retrieved.
   *
   * @return {@code byte[]} - Byte array of the file.
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/resource/{origin}/{fileName:.+}")
  public byte[] getFileAsByteArray(
    @PathVariable
      String origin,
    @PathVariable
      String fileName
  ) {
    
    return fileService.getFileAsByteArray(fileName, FileOrigin.valueOf(
      origin.toUpperCase()));
  }
  
}
