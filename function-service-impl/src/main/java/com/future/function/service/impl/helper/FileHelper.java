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

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileHelper {

  public static final String DOT = ".";

  public static final String PATH_SEPARATOR = File.separator;

  public static void createJavaIoFile(byte[] bytes, String path) {

    FileHelper.toJavaIoFile(bytes, path);
  }

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

  public static void createThumbnail(
    byte[] bytes, String path, String extension
  ) {

    File thumbnailFile = FileHelper.toJavaIoFile(bytes, path);
    try {
      Thumbnails.of(thumbnailFile)
        .size(150, 150)
        .outputFormat(extension)
        .toFile(Objects.requireNonNull(thumbnailFile));
    } catch (Exception e) {
      log.error("Failed creating thumbnail: ", e);
    }
  }

  public static byte[] toByteArray(File file) {

    try {
      return IOUtils.toByteArray(file.toURI());
    } catch (IOException e) {
      return new byte[] {};
    }
  }

  public static boolean isThumbnailName(String name) {

    int maxNonThumbnailFilenameLength =
      36 + DOT.length() + FilenameUtils.getExtension(name)
        .length();
    return name.length() > maxNonThumbnailFilenameLength;
  }

}
