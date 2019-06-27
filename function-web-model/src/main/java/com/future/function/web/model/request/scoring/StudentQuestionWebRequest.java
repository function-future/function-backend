package com.future.function.web.model.request.scoring;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuestionWebRequest {

  @NotNull(message = "NotNull")
  private Integer number;

  @NotBlank(message = "NotBlank")
  private String optionId;

}
