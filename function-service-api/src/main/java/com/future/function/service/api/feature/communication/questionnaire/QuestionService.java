package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Question;

import java.util.List;

public interface QuestionService {

  List<Question> getQuestionByQuestionnaire(String questionnaireId);

  Question getQuestion(String questionId);

  Question createQuestion(Question question);

  Question updateQuestion(Question question);

  void deleteQuestion(String QuestionId);
}
