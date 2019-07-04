package com.future.function.model.enums.scoring;

public enum ScoringType {

    QUIZ("QUIZ"),
    ASSIGNMENT("ASSIGNMENT");

    private String type;

    ScoringType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
