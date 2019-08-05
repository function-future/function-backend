package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuestionWebRequest {

  @NotNull(message = "NotNull")
  private Integer number;

  @NotNull(message = "NotNull")
  private String optionId;

}
