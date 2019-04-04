package com.future.function.web.controller;

import com.future.function.common.enumeration.FileOrigin;
import com.future.function.service.api.feature.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/files")
public class FileController {
  
  private final FileService fileService;
  
  @Autowired
  public FileController(FileService fileService) {
    
    this.fileService = fileService;
  }
  
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
