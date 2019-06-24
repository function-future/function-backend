package com.future.function.web.model.response.feature.communication.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantDescriptionResponse {

  private String id;

  private String name;

  private String university;

  private String role;

  private Integer batch;

}
