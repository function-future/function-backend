package com.future.function.web.model.request.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represent the assignment request in the web as AssignmentWebRequest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentWebRequest {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;

  @NotBlank(message = "NotBlank")
  private String title;

  @NotBlank(message = "NotBlank")
  private String description;

  @NotNull(message = "NotNull")
  private long deadline;

  @NotBlank(message = "NotBlank")
  private String question;

  private List<Long> batch;
}
