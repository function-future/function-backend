package com.future.function.service.impl.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Helper class for file-related operations.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileHelper {
  
  public static final String DOT = ".";
  
  public static final String PATH_SEPARATOR = File.separator;
  
  /**
   * Creates file from {@code byte[]} on specific path.
   *
   * @param bytes Data to be 'inserted' to file.
   * @param path  Path of the created file.
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void createJavaIoFile(byte[] bytes, String path) {
    
    Optional.ofNullable(FileHelper.toJavaIoFile(bytes, path))
      .ifPresent(file -> {
        try {
          log.info("Attempting to create file: {}", file.createNewFile());
        } catch (IOException e) {
          log.error("Failed creating file: ", e);
        }
      });
  }
  
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
      log.error("Failed writing byte array to file: ", e);
      return null;
    }
  }
  
  /**
   * Creates file from {@code byte[]} on specific path with specific extenstion.
   *
   * @param bytes Data to be 'inserted' to file.
   * @param path  Path of the created file.
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void createThumbnail(
    byte[] bytes, String path, String extension
  ) {
    
    File thumbnailFile = FileHelper.toJavaIoFile(bytes, path);
    try {
      Thumbnails.of(thumbnailFile)
        .size(150, 150)
        .outputFormat(extension)
        .toFile(Objects.requireNonNull(thumbnailFile));
      
      log.info(
        "Attempting to create thumbnail: {}", thumbnailFile.createNewFile());
    } catch (IOException | NullPointerException e) {
      log.error("Failed creating thumbnail: ", e);
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
