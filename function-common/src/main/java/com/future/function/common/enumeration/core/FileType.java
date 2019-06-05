package com.future.function.common.enumeration.core;

import java.util.stream.Stream;

public enum FileType {
  FILE(false),
  FOLDER(true);
  
  private boolean markFolder;
  
  FileType(boolean markFolder) {
    
    this.markFolder = markFolder;
  }
  
  public static String getFileType(boolean markFolder) {
    
    return Stream.of(FileType.values())
      .filter(fileType -> fileType.isMarkFolder() == markFolder)
      .findFirst()
      .map(Enum::name)
      .orElse(null);
  }
  
  public boolean isMarkFolder() {
    
    return this.markFolder;
  }
  
  public static boolean isAnyMatch(String type) {
    
    return Stream.of(FileType.values())
      .anyMatch(fileType -> type.equals(fileType.name()));
  }
}
