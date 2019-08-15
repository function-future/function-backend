package com.future.function.web.mapper.response.core.embedded;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.mapper.helper.UrlHelper;
import com.future.function.web.model.response.feature.core.embedded.EmbeddedFileWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmbeddedFileWebResponseMapper {

  public static List<EmbeddedFileWebResponse> toEmbeddedFileWebResponses(
    List<FileV2> files, String urlPrefix
  ) {

    return Optional.ofNullable(files)
      .orElseGet(Collections::emptyList)
      .stream()
      .map(
        file -> EmbeddedFileWebResponseMapper.buildEmbeddedFileWebResponse(file,
                                                                           urlPrefix
        ))
      .collect(Collectors.toList());
  }

  private static EmbeddedFileWebResponse buildEmbeddedFileWebResponse(
    FileV2 fileV2, String urlPrefix
  ) {

    return EmbeddedFileWebResponse.builder()
      .id(fileV2.getId())
      .file(EmbeddedFileWebResponseMapper.
        buildEmbeddedFileWebResponseFile(fileV2, urlPrefix))
      .build();
  }

  private static EmbeddedFileWebResponse.File buildEmbeddedFileWebResponseFile(
    FileV2 fileV2, String urlPrefix
  ) {

    return EmbeddedFileWebResponse.File.builder()
      .full(UrlHelper.toFileUrl(urlPrefix, fileV2.getFileUrl()))
      .thumbnail(UrlHelper.toFileUrl(urlPrefix, fileV2.getThumbnailUrl()))
      .build();
  }

}
