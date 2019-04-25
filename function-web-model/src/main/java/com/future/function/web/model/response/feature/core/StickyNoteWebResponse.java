package com.future.function.web.model.response.feature.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for sticky note web response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickyNoteWebResponse {
  
  private String noteTitle;
  
  private String noteDescription;
  
  private Long updatedAt;
  
}
