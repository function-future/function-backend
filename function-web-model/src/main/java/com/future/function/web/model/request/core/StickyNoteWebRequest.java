package com.future.function.web.model.request.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Model representation for sticky note web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickyNoteWebRequest {
  
  @NotBlank(message = "NotBlank")
  private String noteTitle;
  
  @NotBlank(message = "NotBlank")
  private String noteDescription;
  
}
