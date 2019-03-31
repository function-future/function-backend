package com.future.function.model.entity.feature.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
  
  @NotNull
  private String filePath;
  
  @NotNull
  private String fileUrl;
  
  private String thumbnailPath;
  
  private String thumbnailUrl;
  
}
