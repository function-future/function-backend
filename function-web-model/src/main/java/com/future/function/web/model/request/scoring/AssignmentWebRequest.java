package com.future.function.web.model.request.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentWebRequest {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;

  private String title;

  private String description;

  private long deadline;

  private String question;

  private List<Long> batch;
}
