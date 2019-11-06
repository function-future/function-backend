package com.future.function.web.model.request.scoring;

import com.future.function.validation.annotation.core.BatchMustExist;
import com.future.function.validation.annotation.scoring.QuizMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyQuizWebRequest {

  @BatchMustExist
  @NotBlank(message = "NotBlank")
  private String batchCode;

  @QuizMustExist
  @NotBlank(message = "NotBlank")
  private String quizId;

}
