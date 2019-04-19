package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Model representation for announcement web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWebRequest {
  
  @NotBlank(message = "NotBlank")
  private String announcementTitle;
  
  @Max(value = 70,
       message = "Max")
  private String announcementSummary;
  
  @NotNull(message = "NotNull")
  private String announcementDescriptionHtml;
  
}
