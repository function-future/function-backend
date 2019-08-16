package com.future.function.web.mapper.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlHelper {

  public static String toFileUrl(String urlPrefix, String fileUrl) {

    return urlPrefix + fileUrl;
  }

}
