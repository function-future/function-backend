package com.future.function.web.mapper.request.core;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileRequestMapper {
  
  public Pair<String, byte[]> toStringAndByteArrayPair(
    MultipartFile multipartFile
  ) {
    
    return Pair.of(
      FilenameUtils.getName(multipartFile.getOriginalFilename()),
      toByteArray(multipartFile)
    );
  }
  
  public byte[] toByteArray(MultipartFile multipartFile) {
    
    try {
      return multipartFile.getBytes();
    } catch (IOException e) {
      return new byte[] {};
    }
  }
  
}
