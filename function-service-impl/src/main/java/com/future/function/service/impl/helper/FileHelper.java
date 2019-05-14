package com.future.function.service.impl.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * Helper class for file-related operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileHelper {
  
  public static final String DOT = ".";
  
  /**
   * Converts {@code byte[]} to {@link java.io.File}.
   *
   * @param bytes Data to be 'inserted' to file.
   * @param path  Path of the created file.
   *
   * @return {@code File} - The converted data in form of {@code File}.
   */
  public static File toJavaIoFile(byte[] bytes, String path) {
    
    File file = new File(path);
    try {
      FileUtils.writeByteArrayToFile(file, bytes);
      return file;
    } catch (IOException e) {
      return null;
    }
  }
  
  /**
   * Converts {@link java.io.File} to {@code byte[]}.
   *
   * @param file File to be converted.
   *
   * @return {@code byte[]} - The converted data in form of {@code byte[]}.
   */
  public static byte[] toByteArray(File file) {
    
    try {
      return IOUtils.toByteArray(file.toURI());
    } catch (IOException e) {
      return new byte[] {};
    }
  }
  
  /**
   * Checks if a given file name has suffix '-thumbnail' or not.
   *
   * @param name Name of the file to be checked.
   *
   * @return {@code boolean} - The result of check whether the name contains
   * suffix '-thumbnail' or not.
   */
  public static boolean isThumbnailName(String name) {
    
    int maxNonThumbnailFilenameLength =
      36 + DOT.length() + FilenameUtils.getExtension(name)
        .length();
    return name.length() > maxNonThumbnailFilenameLength;
  }
  
}
