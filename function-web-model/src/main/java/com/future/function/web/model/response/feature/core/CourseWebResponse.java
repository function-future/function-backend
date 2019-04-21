package com.future.function.web.model.response.feature.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for course web response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseWebResponse {
  
  private String courseId;
  
  private String courseTitle;
  
  private String courseDescription;
  
  private String courseThumbnailUrl;
  
  private String courseFileUrl;
  
}
