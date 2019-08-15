package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.core.User;

public interface ScoringMediatorService {

  User createQuizAndAssignmentsByStudent(User user);

  User deleteQuizAndAssignmentsByStudent(User user);

}
