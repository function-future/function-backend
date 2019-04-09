package com.future.function.common.properties.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("file")
public class FileProperties {
  
  private String storagePath;
  
  private String thumbnailSuffix;
  
  private String urlPrefix;
  
  private List<String> imageExtensions;
  
}
