package com.future.function.web.mapper.request.core;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class MultipartFileRequestMapper {

  public Pair<String, byte[]> toStringAndByteArrayPair(
    MultipartFile multipartFile
  ) {

    return Pair.of(this.getOriginalFilename(multipartFile),
                   toByteArray(multipartFile)
    );
  }

  private String getOriginalFilename(MultipartFile multipartFile) {

    return Optional.ofNullable(multipartFile)
      .map(MultipartFile::getOriginalFilename)
      .map(FilenameUtils::getName)
      .orElse("");
  }

  public byte[] toByteArray(MultipartFile multipartFile) {

    try {
      return multipartFile.getBytes();
    } catch (Exception e) {
      return new byte[] {};
    }
  }

}
