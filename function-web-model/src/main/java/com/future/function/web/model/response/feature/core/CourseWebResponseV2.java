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
public class CourseWebResponseV2 {
  
  private String id;
  
  private String title;
  
  private String description;
  
  private String material;
}
