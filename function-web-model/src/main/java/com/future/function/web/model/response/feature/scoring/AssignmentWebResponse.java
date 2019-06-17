package com.future.function.web.model.response.feature.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent the assignment in the web as AssignmentWebResponse
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentWebResponse {

  private String id;

  private String title;

  private String description;

  private long deadline;

  //TODO add file, batch, and rooms later

}
