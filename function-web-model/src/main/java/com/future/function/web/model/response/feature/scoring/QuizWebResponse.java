package com.future.function.web.model.response.feature.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizWebResponse {

  private String id;

  private String title;

  private String description;

  private Long deadline;

  private Long timeLimit;

  private Integer tries;

  private Integer questionCount;

//  private QuestionBank questionBank;

}
