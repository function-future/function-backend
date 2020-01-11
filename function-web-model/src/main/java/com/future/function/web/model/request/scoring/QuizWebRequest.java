package com.future.function.web.model.request.scoring;

import com.future.function.validation.annotation.scoring.DateNotPassed;
import com.future.function.validation.annotation.scoring.QuestionBankMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizWebRequest {

  @NotBlank(message = "NotBlank")
  private String title;

  @NotBlank(message = "NotBlank")
  private String description;

  @DateNotPassed
  @Min(value = 1,
       message = "MinimalOnePositiveNumber")
  private Long startDate;

  @DateNotPassed
  @Min(value = 1,
       message = "MinimalOnePositiveNumber")
  private Long endDate;

  @Min(value = 1,
       message = "MinimalOnePositiveNumber")
  private Long timeLimit;

  @Min(value = 1,
       message = "MinimalOnePositiveNumber")
  private Integer trials;

  @Min(value = 1,
       message = "MinimalOnePositiveNumber")
  private Integer questionCount;

  @QuestionBankMustExist
  @NotNull(message = "NotNull")
  @Size(min = 1,
        message = "Size")
  private List<String> questionBanks;

}
