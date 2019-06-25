package com.future.function.web.model.response.feature.core;

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
  
  private Author author;
  
  private Long createdAt;
  
  /**
   * Model representation for author in discussion web response.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Author {
    
    private String id;
    
    private String name;
    
  }
  
}
