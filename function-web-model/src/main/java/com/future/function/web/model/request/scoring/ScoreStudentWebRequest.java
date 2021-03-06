package com.future.function.web.model.request.scoring;

import com.future.function.validation.annotation.scoring.StudentMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreStudentWebRequest {

  @StudentMustExist
  @NotBlank(message = "NotBlank")
  private String studentId;

  @NotNull(message = "NotNull")
  @Min(message = "Min",
       value = 0)
  private Integer score;

}
