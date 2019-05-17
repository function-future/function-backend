package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankWebRequest {

  @NotEmpty(message = "NotEmpty")
  private String description;

  //TODO uncomment and implement when question feature is created
//  private List<QuestionWebRequest> questions;

}
