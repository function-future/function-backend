package com.future.function.web.model.response.feature.communication.questionnaire;

import com.future.function.web.model.response.feature.core.BatchWebResponse;
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

  private BatchWebResponse batch;

  private String university;
}
