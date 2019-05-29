package com.future.function.web.model.response.feature.core;

import com.future.function.web.model.response.feature.core.embedded.AuthorWebResponse;
import com.future.function.web.model.response.feature.core.embedded.EmbeddedFileWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representation for activity blog web response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBlogWebResponse {
  
  private String id;
  
  private String title;
  
  private String description;
  
  private List<EmbeddedFileWebResponse> files;
  
  private AuthorWebResponse author;
  
}
