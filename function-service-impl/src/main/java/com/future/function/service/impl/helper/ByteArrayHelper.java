package com.future.function.service.impl.helper;

import com.future.function.common.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * Helper class for converting objects to {@code byte[]}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteArrayHelper {
  
  /**
   * Converts {@link java.io.File} to {@code byte[]}.
   *
   * @param file File to be converted.
   *
   * @return {@code byte[]} - The converted data in form of {@code byte[]}.
   */
  public static byte[] getBytesFromJavaIoFile(File file) {
    
    try {
      return IOUtils.toByteArray(file.toURI());
    } catch (IOException e) {
      throw new BadRequestException("Unsupported Operation");
    }
  }
  
}
