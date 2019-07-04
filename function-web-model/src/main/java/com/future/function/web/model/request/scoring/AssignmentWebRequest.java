package com.future.function.web.model.request.scoring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.future.function.validation.annotation.core.FileMustExist;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represent the assignment request in the web as AssignmentWebRequest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentWebRequest {

  private String id;

  @NotBlank(message = "NotBlank")
  private String title;

  @NotBlank(message = "NotBlank")
  private String description;

  @NotNull(message = "NotNull")
  private Long deadline;

  @NotEmpty(message = "NotEmpty")
  private String batchCode;

  @FileMustExist
  @Size(max = 1, message = "Size")
  private List<String> files;
}
