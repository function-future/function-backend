package com.future.function.web.mapper.response.core;

import com.future.function.common.enumeration.core.FileType;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.FilePath;
import com.future.function.model.entity.feature.core.embedded.Version;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.embedded.AuthorWebResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.DataPageResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import com.future.function.web.model.response.feature.core.embedded.PathWebResponse;
import com.future.function.web.model.response.feature.core.embedded.VersionWebResponse;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
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

  public static DataResponse<FileWebResponse<FileContentWebResponse>> toSingleFileDataResponse(
    FileV2 file, String urlPrefix
  ) {

    return FileResponseMapper.toSingleFileDataResponse(
      HttpStatus.OK, file, urlPrefix);
  }

  public static DataResponse<FileWebResponse<FileContentWebResponse>> toSingleFileDataResponse(
    HttpStatus httpStatus, FileV2 file, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(httpStatus,
                                         FileResponseMapper.buildFileWebResponse(
                                           file, urlPrefix)
    );
  }

  private static FileWebResponse<FileContentWebResponse> buildFileWebResponse(
    FileV2 file, String urlPrefix
  ) {

    return FileWebResponse.<FileContentWebResponse>builder().paths(
      FileResponseMapper.toPathWebResponses(file.getPaths()))
      .content(buildNormalFileContentWebResponse(file, urlPrefix))
      .build();
  }

  private static FileContentWebResponse buildNormalFileContentWebResponse(
    FileV2 file, String urlPrefix
  ) {

    return FileContentWebResponse.builder()
      .id(file.getId())
      .name(file.getName())
      .type(FileType.getFileType(file.isMarkFolder()))
      .parentId(file.getParentId())
      .versions(FileResponseMapper.toFileWebResponseVersions(file.getVersions(),
                                                             urlPrefix
      ))
      .file(FileResponseMapper.getFileUrl(file, urlPrefix))
      .author(FileResponseMapper.toAuthorWebResponse(file))
      .build();
  }

  private static AuthorWebResponse toAuthorWebResponse(FileV2 file) {

    return Optional.ofNullable(file.getUser())
      .map(AuthorWebResponseMapper::buildAuthorWebResponse)
      .orElse(null);
  }

  private static Map<Long, VersionWebResponse> toFileWebResponseVersions(
    Map<Long, Version> fileVersions, String urlPrefix
  ) {

    Map<Long, VersionWebResponse> versions = new LinkedHashMap<>();

    List<Long> versionKeys = new ArrayList<>(fileVersions.keySet());
    Collections.reverse(versionKeys);

    versionKeys.forEach(key -> versions.put(key,
                                            FileResponseMapper.toVersionWebResponse(
                                              fileVersions.get(key), urlPrefix)
    ));

    return versions;
  }

  private static VersionWebResponse toVersionWebResponse(
    Version version, String urlPrefix
  ) {

    return new VersionWebResponse(version.getTimestamp(),
                                  urlPrefix + version.getUrl()
    );
  }

  private static String getFileUrl(FileV2 file, String urlPrefix) {

    return Optional.of(file)
      .map(FileV2::getFileUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }

  private static List<PathWebResponse> toPathWebResponses(List<FilePath> paths) {

    return paths.stream()
      .map(FileResponseMapper::buildPathWebResponse)
      .collect(Collectors.toList());
  }

  private static PathWebResponse buildPathWebResponse(FilePath path) {

    return PathWebResponse.builder()
      .id(path.getId())
      .name(path.getName())
      .build();
  }

  public static DataPageResponse<FileWebResponse<List<FileContentWebResponse>>> toMultipleFileDataResponse(
    Pair<List<FilePath>, Page<FileV2>> data, String urlPrefix
  ) {

    return DataPageResponse.<FileWebResponse<List<FileContentWebResponse>>>builder().code(
      HttpStatus.OK.value())
      .status(HttpStatus.OK.name())
      .data(FileResponseMapper.buildFileWebResponse(data.getFirst(),
                                                    data.getSecond(), urlPrefix
      ))
      .paging(PageHelper.toPaging(data.getSecond()))
      .build();
  }

  private static FileWebResponse<List<FileContentWebResponse>> buildFileWebResponse(
    List<FilePath> paths, Page<FileV2> data, String urlPrefix
  ) {

    return FileWebResponse.<List<FileContentWebResponse>>builder().paths(
      FileResponseMapper.toPathWebResponses(paths))
      .content(FileResponseMapper.toFileContentWebResponses(data, urlPrefix))
      .build();
  }

  private static List<FileContentWebResponse> toFileContentWebResponses(
    Page<FileV2> data, String urlPrefix
  ) {

    return data.getContent()
      .stream()
      .map(file -> FileResponseMapper.buildThumbnailFileContentWebResponse(file,
                                                                           urlPrefix
      ))
      .collect(Collectors.toList());
  }

  private static FileContentWebResponse buildThumbnailFileContentWebResponse(
    FileV2 file, String urlPrefix
  ) {

    return FileContentWebResponse.builder()
      .id(file.getId())
      .name(file.getName())
      .type(FileType.getFileType(file.isMarkFolder()))
      .parentId(file.getParentId())
      .versions(FileResponseMapper.toFileWebResponseVersions(file.getVersions(),
                                                             urlPrefix
      ))
      .file(FileResponseMapper.getFileUrl(file, urlPrefix))
      .author(FileResponseMapper.toAuthorWebResponse(file))
      .build();
  }

}
