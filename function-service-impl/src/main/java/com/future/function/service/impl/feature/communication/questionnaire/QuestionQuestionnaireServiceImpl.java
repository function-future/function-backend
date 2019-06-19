package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.repository.feature.communication.questionnaire.QuestionQuestionnaireRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionQuestionnaireService;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class QuestionQuestionnaireServiceImpl implements QuestionQuestionnaireService {

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;

  private final QuestionnaireService questionnaireService;

  @Autowired
  public QuestionQuestionnaireServiceImpl(QuestionQuestionnaireRepository questionQuestionnaireRepository, QuestionnaireService questionnaireService) {
    this.questionQuestionnaireRepository = questionQuestionnaireRepository;
    this.questionnaireService = questionnaireService;
  }

  @Override
  public List<QuestionQuestionnaire> getQuestionQuestionnaireByQuestionnaire(String questionnaireId) {
    return Optional.of(questionnaireId)
            .map(questionnaireService::getQuestionnaire)
            .map(questionQuestionnaireRepository::findAllByQuestionnaire)
            .orElseThrow(() -> new NotFoundException("Questions Questionnaire not found"));
  }

  @Override
  public QuestionQuestionnaire getQuestionQuestionnaire(String questionQuestionnaireId) {
    return Optional.of(questionQuestionnaireId)
            .map(this.questionQuestionnaireRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Question not Found"));
  }

  @Override
  public QuestionQuestionnaire createQuestionQuestionnaire(QuestionQuestionnaire questionQuestionnaire) {
    return Optional.of(questionQuestionnaire)
            .map(target -> this.setQuestionnaire(questionQuestionnaire,target))
            .map(questionQuestionnaireRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  @Override
  public QuestionQuestionnaire updateQuestionQuestionnaire(QuestionQuestionnaire questionQuestionnaire) {
//    return Optional.of(questionQuestionnaire)
//            .map(QuestionQuestionnaire::getId)
//            .map(questionQuestionnaireRepository::findOne)
//            .map(target -> this.setQuestionnaire(questionQuestionnaire, target))
//            .map(target -> this.copyProperties(questionQuestionnaire,target))
//            .map(questionQuestionnaireRepository::save)
//            .orElse(questionQuestionnaire);
    return null;
  }

  @Override
  public void deleteQuestionQuestionnaire (String questionQuestionnaireId) {
    Optional.ofNullable(questionQuestionnaireId)
      .map(questionQuestionnaireRepository::findOne)
      .ifPresent(this::softDeletedHelper);

  }

  private QuestionQuestionnaire setQuestionnaire (QuestionQuestionnaire questionQuestionnaire, QuestionQuestionnaire targetQuestionQuestionnaire) {
    targetQuestionQuestionnaire.setQuestionnaire(
      questionnaireService.getQuestionnaire(questionQuestionnaire.getQuestionnaire().getId())
    );
    return targetQuestionQuestionnaire;
  }

  private QuestionQuestionnaire copyProperties (QuestionQuestionnaire questionQuestionnaire, QuestionQuestionnaire targetQuestionQuestionnaire) {
    BeanUtils.copyProperties(questionQuestionnaire, targetQuestionQuestionnaire);
    return targetQuestionQuestionnaire;
  }

  private void softDeletedHelper (QuestionQuestionnaire questionQuestionnaire) {
    questionQuestionnaire.setDeleted(true);
    questionQuestionnaire.setQuestionnaire(null);
    questionQuestionnaireRepository.save(questionQuestionnaire);
  }
}
