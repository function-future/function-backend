package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionQuestionnaireRepository
  extends MongoRepository<QuestionQuestionnaire, String> {

  List<QuestionQuestionnaire> findAllByQuestionnaire(
    Questionnaire questionnaire
  );

}
