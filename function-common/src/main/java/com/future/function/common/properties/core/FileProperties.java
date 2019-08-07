package com.future.function.common.properties.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

  private String storagePath = "C:\\function\\files";

  private String thumbnailSuffix = "-thumbnail";

  private String urlPrefix = "";

  private List<String> imageExtensions = Arrays.asList(".jpg", ".jpeg", ".png");

  private String rootId = "root";

  private long schedulerActivePeriod = 86400000L;

  private long minimumFileCreatedPeriod = 21600000L;

}
