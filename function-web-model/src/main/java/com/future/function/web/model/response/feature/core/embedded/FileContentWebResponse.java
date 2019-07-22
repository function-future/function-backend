package com.future.function.web.model.response.feature.core.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileContentWebResponse {
  
  private String id;
  
  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private String type;
  
  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private String name;
  
  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private Object file;
  
  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private Map<Long, VersionWebResponse> versions;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String parentId;
  
  private AuthorWebResponse author;
  
}
