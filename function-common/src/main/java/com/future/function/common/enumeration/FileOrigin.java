package com.future.function.common.enumeration;

/**
 * Enum containing origins of file.
 */
public enum FileOrigin {
  USER(true);
  
  private String lowCaseValue;
  
  private boolean asResource;
  
  FileOrigin(boolean asResource) {
    
    this.lowCaseValue = this.name()
      .toLowerCase();
    this.asResource = asResource;
  }
  
  public String lowCaseValue() {
    
    return this.lowCaseValue;
  }
  
  public boolean isAsResource() {
    
    return this.asResource;
  }
  
}
