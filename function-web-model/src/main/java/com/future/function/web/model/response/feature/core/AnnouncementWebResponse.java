package com.future.function.web.model.response.feature.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for announcement web response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWebResponse {
  
  private String announcementId;
  
  private String announcementTitle;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String announcementSummary;
  
  private String announcementDescriptionHtml;
  
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String announcementFileUrl;
  
  private Long createdAt;
  
  private Long updatedAt;
  
}
