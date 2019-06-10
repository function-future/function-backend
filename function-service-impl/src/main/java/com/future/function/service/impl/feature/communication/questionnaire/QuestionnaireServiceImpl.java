package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

  private final UserService userService;

  private final QuestionnaireRepository questionnaireRepository;

  @Autowired
  public QuestionnaireServiceImpl(UserService userService, QuestionnaireRepository questionnaireRepository){
    this.userService = userService;
    this.questionnaireRepository = questionnaireRepository;
  }

  @Override
  public Page<Questionnaire> getQuestionnaires(Pageable pageable) {
    return this.questionnaireRepository.findAll(pageable);
  }

  @Override
  public Page<Questionnaire> getQuestionnaireWithKeyword(String keyword, Pageable pageable) {
    return this.questionnaireRepository.findAllByTitleIgnoreCaseContaining(keyword, pageable);
  }

  @Override
  public Page<Questionnaire> getQuestionnaireBelongToUser(String userId, Pageable pageable) {
    return null;
  }

  @Override
  public Questionnaire getQuestionnaire(String questionnaireId) {
    return Optional.ofNullable(questionnaireId)
            .map(this.questionnaireRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Get Questionnaire by id Not Found"));
  }

  @Override
  public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
    return Optional.of(questionnaire)
            .map(questionnaireRepository::save)
            .orElse(questionnaire);
  }

  @Override
  public Questionnaire updateQuestionnaire(Questionnaire questionnaire) {
    return null;
  }

  @Override
  public void deleteQuestionnaire(String QuestionnaireId) {

  }
}
