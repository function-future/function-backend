package com.future.function.web.mapper.response.core;

import com.future.function.common.enumeration.core.FileType;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.Version;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.embedded.AuthorWebResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import com.future.function.web.model.response.feature.core.embedded.VersionWebResponse;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileResponseMapper {
  
  public static PagingResponse<FileWebResponse> toFilePagingResponse(
    Page<FileV2> files
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           FileResponseMapper.toFileWebResponseList(
                                             files), PageHelper.toPaging(files)
    );
  }
  
  private static List<FileWebResponse> toFileWebResponseList(
    Page<FileV2> files
  ) {
    
    return files.getContent()
      .stream()
      .map(FileResponseMapper::buildThumbnailFileWebResponse)
      .collect(Collectors.toList());
  }
  
  public static DataResponse<FileWebResponse> toFileDataResponse(
    FileV2 file
  ) {
    
    return FileResponseMapper.toFileDataResponse(HttpStatus.OK, file);
  }
  
  public static DataResponse<FileWebResponse> toFileDataResponse(
    HttpStatus httpStatus, FileV2 file
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         FileResponseMapper.buildNormalFileWebResponse(
                                           file)
    );
  }
  
  private static FileWebResponse buildNormalFileWebResponse(FileV2 file) {
    
    return FileWebResponse.builder()
      .id(file.getId())
      .name(file.getName())
      .type(FileType.getFileType(file.isMarkFolder()))
      .parentId(file.getParentId())
      .versions(
        FileResponseMapper.toFileWebResponseVersions(file.getVersions()))
      .file(FileResponseMapper.getFileUrl(file))
      .author(toAuthorWebResponse(file))
      .build();
  }
  
  private static AuthorWebResponse toAuthorWebResponse(FileV2 file) {
    
    return Optional.ofNullable(file.getUser())
      .map(AuthorWebResponseMapper::buildAuthorWebResponse)
      .orElse(null);
  }
  
  private static Map<Long, VersionWebResponse> toFileWebResponseVersions(
    Map<Long, Version> fileVersions
  ) {
    
    Map<Long, VersionWebResponse> versions = new LinkedHashMap<>();
    
    List<Long> versionKeys = new ArrayList<>(fileVersions.keySet());
    Collections.reverse(versionKeys);
  
    versionKeys.forEach(key -> versions.put(key,
                                            FileResponseMapper.toVersionWebResponse(
                                              fileVersions.get(key))
    ));
    
    return versions;
  }
  
  private static VersionWebResponse toVersionWebResponse(Version version) {
    
    return new VersionWebResponse(version.getTimestamp(), version.getUrl());
  }
  
  private static String getFileUrl(FileV2 file) {
    
    return Optional.of(file)
      .map(FileV2::getFileUrl)
      .orElse(null);
  }
  
  private static FileWebResponse buildThumbnailFileWebResponse(FileV2 file) {
    
    return FileWebResponse.builder()
      .id(file.getId())
      .name(file.getName())
      .type(FileType.getFileType(file.isMarkFolder()))
      .parentId(file.getParentId())
      .versions(
        FileResponseMapper.toFileWebResponseVersions(file.getVersions()))
      .file(FileResponseMapper.getThumbnailUrl(file))
      .author(toAuthorWebResponse(file))
      .build();
  }
  
  private static String getThumbnailUrl(FileV2 file) {
    
    return Optional.of(file)
      .map(FileV2::getThumbnailUrl)
      .orElseGet(() -> FileResponseMapper.getFileUrl(file));
  }
  
}
