package com.future.function.common.enumeration.core;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum containing origins of file.
 * <p>
 * This enum class will be useful in determining whether a file serves as a
 * resource (static file) or a dynamic file (versioned). This class also serves
 * as the name of root folder for a file in the file storage.
 */
public enum FileOrigin {
  USER(true),
  ANNOUNCEMENT(true),
  COURSE(true),
  BLOG(true),
  FILE(false),
  UNKNOWN(false);
  
  private String lowCaseValue;
  
  private boolean asResource;
  
  FileOrigin(boolean asResource) {
    
    this.lowCaseValue = this.name()
      .toLowerCase();
    this.asResource = asResource;
  }
  
  /**
   * Converts a String to a specific file origin. Customized to prevent
   * {@link IllegalStateException} from being thrown when using
   * {@link #valueOf(String)} method. If the given parameter is not
   * recognized as a value of FileOrigin enums, then {@link #UNKNOWN} will be
   * returned.
   *
   * @param origin The name of the file origin (in form of String) to be
   *               converted to FileOrigin enum value.
   *
   * @return {@link FileOrigin} - The FileOrigin enum value of the given
   * parameter.
   */
  public static FileOrigin toFileOrigin(String origin) {
    
    return Optional.ofNullable(origin)
      .map(String::toUpperCase)
      .filter(FileOrigin::isOriginEqualsAnyFileOrigin)
      .map(FileOrigin::valueOf)
      .orElse(UNKNOWN);
  }
  
  /**
   * Compares whether the given origin is equal to any FileOrigin enum in this
   * enum class.
   *
   * @param origin The name of the file origin (in form of String) to be
   *               converted to FileOrigin enum value.
   *
   * @return {@code boolean} - Result of whether any file origin is equal to the
   * given parameter.
   */
  private static boolean isOriginEqualsAnyFileOrigin(String origin) {
    
    return Stream.of(FileOrigin.values())
      .anyMatch(role -> origin.equals(role.name()));
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
