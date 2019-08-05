package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import com.future.function.web.model.util.constant.FieldName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceResponseMapper {
  
  public static DataResponse<FileContentWebResponse> toResourceDataResponse(
    FileV2 fileV2, String urlPrefix
  ) {
  
    FileContentWebResponse resourceWebResponse = buildFileWebResponse(fileV2,
                                                                      urlPrefix);
    
    return ResponseHelper.toDataResponse(HttpStatus.CREATED,
                                         resourceWebResponse
    );
  }
  
  public static FileContentWebResponse buildFileWebResponse(FileV2 fileV2, String urlPrefix) {
    
    return FileContentWebResponse.builder()
      .id(fileV2.getId())
      .name(fileV2.getName())
      .file(ResourceResponseMapper.toUrlMap(fileV2, urlPrefix))
      .build();
  }
  
  private static Map<String, String> toUrlMap(
    FileV2 fileV2, String urlPrefix
  ) {
    
    Map<String, String> map = new HashMap<>();
    
    map.put(FieldName.FULL, urlPrefix + fileV2.getFileUrl());
    Optional.of(fileV2)
      .map(FileV2::getThumbnailUrl)
      .map(urlPrefix::concat)
      .ifPresent(url -> map.put(FieldName.THUMBNAIL, url));
    
    return map;
  }
  
}
