package com.future.function.web.model.response.feature.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.future.function.web.model.response.feature.core.embedded.EmbeddedFileWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWebResponse {

  private String id;

  private String title;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String summary;
  
  private String description;
  
  private List<EmbeddedFileWebResponse> files;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String announcementFileUrl;
  
  private Long updatedAt;
  
}
