package com.future.function.web.model.response.feature.scoring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentQuizDetailWebResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<StudentQuestionWebResponse> questions;

  private Integer point;

  private Integer trials;

}
