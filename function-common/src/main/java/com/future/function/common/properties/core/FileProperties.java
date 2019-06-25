package com.future.function.common.properties.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Properties class for getting properties from {@code application
 * .properties} file, section {@literal # File Properties}.
 * <p>
 * Useful when a class requires values specified in the {@code application
 * .properties} file for the class' operation(s).
 */
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
  
  private String rootId;
  
  private long schedulerActivePeriod;
  
  private long minimumFileCreatedPeriod;
  
}
