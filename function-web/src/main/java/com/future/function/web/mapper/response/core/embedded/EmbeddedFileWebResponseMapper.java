package com.future.function.web.mapper.response.core.embedded;

import com.future.function.model.entity.feature.core.FileV2;
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
    List<FileV2> files
  ) {
    
    return Optional.ofNullable(files)
      .orElseGet(Collections::emptyList)
      .stream()
      .map(EmbeddedFileWebResponseMapper::buildEmbeddedFileWebResponse)
      .collect(Collectors.toList());
  }
  
  private static EmbeddedFileWebResponse buildEmbeddedFileWebResponse(
    FileV2 fileV2
  ) {
    
    return EmbeddedFileWebResponse.builder()
      .id(fileV2.getId())
      .file(EmbeddedFileWebResponseMapper.
        buildEmbeddedFileWebResponseFile(fileV2))
      .build();
  }
  
  private static EmbeddedFileWebResponse.File buildEmbeddedFileWebResponseFile(
    FileV2 fileV2
  ) {
    
    return EmbeddedFileWebResponse.File.builder()
      .full(fileV2.getFileUrl())
      .thumbnail(fileV2.getThumbnailUrl())
      .build();
  }
  
}
