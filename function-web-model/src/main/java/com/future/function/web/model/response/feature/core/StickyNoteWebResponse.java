package com.future.function.web.model.response.feature.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickyNoteWebResponse {
  
  private String id;
  
  private String title;
  
  private String description;
  
  private Long updatedAt;
  
}
