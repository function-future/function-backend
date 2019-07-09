package com.future.function.web.model.request.communication.questionnaire;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseRequest {

  @NotNull(message = "NotNull")
  private String idQuestion;

  @NotNull(message = "NotNull")
  private Float score;

  @NotBlank(message = "NotBlank")
  @Size(message = "CommentSize", max = 200)
  private String comment;
}
