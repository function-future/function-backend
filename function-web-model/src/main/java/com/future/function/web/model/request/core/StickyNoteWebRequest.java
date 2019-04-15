package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for sticky note web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickyNoteWebRequest {
  
  private String noteTitle;
  
  private String noteDescription;
  
}
