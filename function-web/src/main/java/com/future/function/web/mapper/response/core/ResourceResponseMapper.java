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
    FileV2 fileV2
  ) {
  
    FileContentWebResponse resourceWebResponse = buildFileWebResponse(fileV2);
    
    return ResponseHelper.toDataResponse(HttpStatus.CREATED,
                                         resourceWebResponse
    );
  }
  
  public static FileContentWebResponse buildFileWebResponse(FileV2 fileV2) {
    
    return FileContentWebResponse.builder()
      .id(fileV2.getId())
      .name(fileV2.getName())
      .file(ResourceResponseMapper.toUrlMap(fileV2))
      .build();
  }
  
  private static Map<String, String> toUrlMap(FileV2 fileV2) {
    
    Map<String, String> map = new HashMap<>();
    
    map.put(FieldName.FULL, fileV2.getFileUrl());
    Optional.of(fileV2)
      .map(FileV2::getThumbnailUrl)
      .ifPresent(url -> map.put(FieldName.THUMBNAIL, url));
    
    return map;
  }
  
}
