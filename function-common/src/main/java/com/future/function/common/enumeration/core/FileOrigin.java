package com.future.function.common.enumeration.core;

/**
 * Enum containing origins of file.
 * <p>
 * This enum class will be useful in determining whether a file serves as a
 * resource (static file) or a dynamic file (versioned). This class also serves
 * as the name of root folder for a file in the file storage.
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
  
  /**
   * Returns the lower case value of {@link #name()} method of the selected
   * enum. Useful when creating folder for file operation purposes.
   *
   * @return {@code String} - The lower case value of the {@code name()}
   * method of the selected enum.
   */
  public String lowCaseValue() {
    
    return this.lowCaseValue;
  }
  
  /**
   * Returns the mark {@link #asResource} of the enum to mark whether this
   * enum will mark other objects as resource or as dynamic file.
   *
   * @return {@code boolean} - The mark as resource for the selected enum.
   */
  public boolean isAsResource() {
    
    return this.asResource;
  }
  
}
