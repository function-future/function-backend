package com.future.function.web.model.response.feature.core.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedFileWebResponse {

  private String id;

  private File file;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class File {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String full;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String thumbnail;

  }

}
