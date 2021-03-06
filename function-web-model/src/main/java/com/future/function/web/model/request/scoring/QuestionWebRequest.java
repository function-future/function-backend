package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWebRequest {

  @NotBlank(message = "NotBlank")
  private String label;

  @NotEmpty(message = "NotEmpty")
  @Size(message = "Size",
        max = 4,
        min = 4)
  private List<OptionWebRequest> options;

}
