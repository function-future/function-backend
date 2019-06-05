package com.future.function.web.mapper.enumeration;

import java.util.stream.Stream;

public enum FileType {
  FILE(false),
  FOLDER(true);
  
  private boolean asResource;
  
  FileType(boolean asResource) {
    
    this.asResource = asResource;
  }
  
  public static String getFileType(boolean asResource) {
    
    return Stream.of(FileType.values())
      .filter(fileType -> fileType.isAsResource() == asResource)
      .findFirst()
      .map(Enum::name)
      .orElse("UNKNOWN");
  }
  
  public boolean isAsResource() {
    
    return asResource;
  }
}
