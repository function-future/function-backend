package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.embedded.Version;
import com.future.function.web.model.response.feature.core.embedded.VersionWebResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileResponseMapperTest {
  
  @Test
  public void a() {
    
    Map<Long, Version> fileVersions = new LinkedHashMap<>();
    
    fileVersions.put(
      1L, new Version(System.currentTimeMillis(), "path-1", "url-1"));
    fileVersions.put(
      2L, new Version(System.currentTimeMillis(), "path-2", "url-2"));
    
    Map<Long, VersionWebResponse> versions = toFileWebResponseVersions(
      fileVersions);
    
    System.out.println(versions);
  }
  
  private Map<Long, VersionWebResponse> toFileWebResponseVersions(
    Map<Long, Version> fileVersions
  ) {
    
    Map<Long, VersionWebResponse> versions = new HashMap<>();
    
    fileVersions.forEach((key, value) -> versions.put(key, toVersionWebResponse(
      value)
    ));
    
    
    return versions;
  }
  
  private VersionWebResponse toVersionWebResponse(Version value) {
    
    return new VersionWebResponse(
      value.getTimestamp(),
      value.getUrl()
    );
  }
  
}
