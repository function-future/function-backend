package com.future.function.model.enums.scoring;

import lombok.Getter;

@Getter
public enum ScoringType {

  QUIZ("QUIZ"),
  ASSIGNMENT("ASSIGNMENT");

  private String type;

  ScoringType(String type) {

    this.type = type;
  }
}
