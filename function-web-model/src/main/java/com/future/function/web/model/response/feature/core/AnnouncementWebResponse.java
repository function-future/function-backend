package com.future.function.web.model.response.feature.core;

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
  
  private String announcementSummary;
  
  private String announcementDescriptionHtml;
  
  private String announcementFileUrl;
  
  private Long createdAt;
  
  private Long updatedAt;
  
}
