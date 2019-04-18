package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for announcement web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWebRequest {
  
  private String announcementTitle;
  
  private String announcementSummary;
  
  private String announcementDescriptionHtml;
  
}
