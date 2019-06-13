package com.future.function.web.model.response.feature.core;

import com.future.function.web.model.response.base.AuthorWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representation for discussion web response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionWebResponse {
  
  private String id;
  
  private String comment;

  private AuthorWebResponse author;
  
  private Long createdAt;
  
}
