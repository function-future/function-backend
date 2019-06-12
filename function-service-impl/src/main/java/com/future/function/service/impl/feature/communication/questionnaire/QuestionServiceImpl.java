package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.repository.feature.communication.questionnaire.QuestionRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionService;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;

  private final QuestionnaireService questionnaireService;

  @Autowired
  public QuestionServiceImpl(QuestionRepository questionRepository, QuestionnaireService questionnaireService) {
    this.questionRepository = questionRepository;
    this.questionnaireService = questionnaireService;
  }

  @Override
  public List<Question> getQuestionByQuestionnaire(String questionnaireId) {
    Questionnaire questionnaire = this.questionnaireService.getQuestionnaire(questionnaireId);

    return questionRepository.findAllByQuestionnaire(questionnaire);
  }

  @Override
  public Question getQuestion(String questionId) {
    return Optional.ofNullable(questionId)
            .map(questionRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Get Question by id Not Found"));
  }

  @Override
  public Question createQuestion(Question question) {
    return Optional.of(question)
            .map(questionRepository::save)
            .orElse(question);
  }

  @Override
  public Question updateQuestion(Question question) {
    return Optional.of(Question)
            .map(Question::getId)
            .map();
  }

  @Override
  public void deleteQuestion(String QuestionId) {

  }
}
