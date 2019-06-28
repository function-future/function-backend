package com.future.function.web.model.request.scoring;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyStudentQuizWebRequest {

  @NotNull(message = "NotNull")
  private int originBatch;

  @NotNull(message = "NotNull")
  private int targetBatch;

  @NotEmpty(message = "Not Empty")
  private String quizId;

}
