package com.future.function.web.model.request.scoring;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizWebRequest {

  @NotBlank(message = "NotBlank")
  private String title;

  @NotBlank(message = "NotBlank")
  private String description;

  @Min(value = 1, message = "MinimalOnePositiveNumber")
  private Long deadline;

  @Min(value = 1, message = "MinimalOnePositiveNumber")
  private Long timeLimit;

  @Min(value = 1, message = "MinimalOnePositiveNumber")
  private Integer tries;

  @Min(value = 1, message = "MinimalOnePositiveNumber")
  private Integer questionCount;

}
