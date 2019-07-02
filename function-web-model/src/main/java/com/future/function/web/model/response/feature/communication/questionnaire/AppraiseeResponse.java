package com.future.function.web.model.response.feature.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppraiseeResponse {

  private String id;

  private String name;

  private String avatar;

  private BatchResponse batch;

  private String university;
}
