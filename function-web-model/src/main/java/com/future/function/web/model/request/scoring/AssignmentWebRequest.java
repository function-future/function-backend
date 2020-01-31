package com.future.function.web.model.request.scoring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.future.function.validation.annotation.core.FileMustExist;
import com.future.function.validation.annotation.scoring.ExtensionMustBeValid;
import com.future.function.validation.annotation.scoring.DateNotPassed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentWebRequest {

  @NotBlank(message = "NotBlank")
  private String title;

  @NotBlank(message = "NotBlank")
  private String description;

  @DateNotPassed
  @NotNull(message = "NotNull")
  private Long deadline;

  @ExtensionMustBeValid(extensions = {"docx", "pdf", "doc"})
  @FileMustExist
  @Size(max = 1,
        message = "Size")
  private List<String> files;

}
