package com.future.function.common.enumeration.core;

import java.util.Optional;
import java.util.stream.Stream;

public enum FileOrigin {
  USER(true),
  ANNOUNCEMENT(true),
  COURSE(true),
  BLOG(true),
  FILE(false),
    UNKNOWN(false),
    ASSIGNMENT(true);
  
  private String lowCaseValue;
  
  private boolean asResource;
  
  FileOrigin(boolean asResource) {
    
    this.lowCaseValue = this.name()
      .toLowerCase();
    this.asResource = asResource;
  }
  
  public static FileOrigin toFileOrigin(String origin) {
    
    return Optional.ofNullable(origin)
      .map(String::toUpperCase)
      .filter(FileOrigin::isOriginEqualsAnyFileOrigin)
      .map(FileOrigin::valueOf)
      .orElse(UNKNOWN);
  }
  
  private static boolean isOriginEqualsAnyFileOrigin(String origin) {
    
    return Stream.of(FileOrigin.values())
      .anyMatch(role -> origin.equals(role.name()));
  }
  
  public String lowCaseValue() {
    
    return this.lowCaseValue;
  }
  
  public boolean isAsResource() {
    
    return this.asResource;
  }
  
}
