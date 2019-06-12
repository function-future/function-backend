package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.FileMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Model representation for activity blog web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBlogWebRequest {
  
  @NotBlank(message = "NotBlank")
  private String title;
  
  @NotBlank(message = "NotBlank")
  private String description;
  
  @FileMustExist
  private List<String> files;
  
}