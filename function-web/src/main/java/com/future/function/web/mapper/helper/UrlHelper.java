package com.future.function.web.mapper.helper;

import com.future.function.common.properties.core.FileProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlHelper {
  
  public static String toFileUrl(
    FileProperties fileProperties, String fileUrl
  ) {
    
    return fileProperties.getUrlPrefix() + fileUrl;
  }
  
}
