package com.future.function.model.entity.feature.core.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilePath {

  private String id;

  @Builder.Default
  private String name = "";

}
