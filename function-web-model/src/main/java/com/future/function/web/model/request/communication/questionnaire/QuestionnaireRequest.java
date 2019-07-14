package com.future.function.web.model.request.communication.questionnaire;

import com.future.function.validation.annotation.communication.QuestionnaireTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireRequest {

  @QuestionnaireTitle(message = "InvalidTitle")
  private String title;

  @Size(max = 50, message = "Size")
  private String desc;

  @NotNull(message = "StartDate")
  private Long startDate;

  @NotNull(message = "DueDate")
  private Long dueDate;


}
