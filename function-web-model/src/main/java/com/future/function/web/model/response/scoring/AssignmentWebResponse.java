package com.future.function.web.model.response.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentWebResponse {

  private String id;

  private String title;

  private String description;

  private long deadline;

  private String question;

}
