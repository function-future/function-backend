package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;

import java.util.List;

public interface UserQuestionnaireSummaryRepositoryCustom {

  List<UserQuestionnaireSummary> findAllByUserName(String name);

}
