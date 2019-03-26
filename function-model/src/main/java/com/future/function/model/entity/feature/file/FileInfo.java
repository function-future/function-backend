package com.future.function.model.entity.feature.file;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
