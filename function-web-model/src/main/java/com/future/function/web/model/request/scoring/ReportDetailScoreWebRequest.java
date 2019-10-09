package com.future.function.web.model.request.scoring;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailScoreWebRequest {

  @Size(message = "Size",
        min = 1)
  @Valid
  private List<ScoreStudentWebRequest> scores;

}
