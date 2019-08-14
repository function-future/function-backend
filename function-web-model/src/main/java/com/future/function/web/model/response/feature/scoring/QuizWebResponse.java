package com.future.function.web.model.response.feature.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizWebResponse {

  private String id;

  private String title;

  private String description;

  private Long startDate;

  private Long endDate;

  private Long timeLimit;

  private Integer trials;

  private Integer questionCount;

  private List<QuestionBankWebResponse> questionBanks;

  private String batchCode;

}
