package com.future.function.model.entity.feature.communication.questionnaire;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

  private String id;

  private float minimum;

  private float maximum;

  private float average;
}
