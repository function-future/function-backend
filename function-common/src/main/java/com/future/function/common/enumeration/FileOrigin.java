package com.future.function.common.enumeration;

/**
 * Enum containing origins of file.
 */
public enum FileOrigin {
  USER(true);
  
  private boolean asResource;
  
  FileOrigin(boolean asResource) {
    
    this.asResource = asResource;
  }
  
  public boolean isAsResource() {
    
    return this.asResource;
  }
}
