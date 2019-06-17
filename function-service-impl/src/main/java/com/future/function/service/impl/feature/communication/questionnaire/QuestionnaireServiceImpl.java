package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class QuestionnaireServiceImpl implements QuestionnaireService {

  private final QuestionnaireRepository questionnaireRepository;

  @Autowired
  public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository) {
    this.questionnaireRepository = questionnaireRepository;
  }

  @Override
  public Page<Questionnaire> getAllQuestionnaires(Pageable pageable) {
    return null;
  }

  @Override
  public Page<Questionnaire> getQuestionnairesWithKeyword(String keyword, Pageable pageable) {
    return null;
  }

  @Override
  public Page<Questionnaire> getQuestionnairesBelongToAppraisee(String appraoseeId, Pageable pageable) {
    return null;
  }

  @Override
  public Questionnaire getQuestionnaire(String questionnaireId) {
    return Optional.of(questionnaireId)
            .map(this.questionnaireRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Questionnaire not Found"));
  }

  @Override
  public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
    return Optional.of(questionnaire)
            .map(questionnaireRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  @Override
  public Questionnaire updateQuestionnaire(Questionnaire questionnaire) {
    return
      Optional.of(questionnaire)
        .map(Questionnaire::getId)
        .map(questionnaireRepository::findOne)
        .map(temp -> this.copyProperties(questionnaire,temp))
        .map(questionnaireRepository::save)
        .orElse(questionnaire);
  }

  @Override
  public void deleteQuestionnaire(String questionnaireId) {
    Optional.ofNullable(questionnaireId)
      .map(questionnaireRepository::findOne)
      .ifPresent(this::softDeleteHelper);
  }

  private Questionnaire copyProperties(Questionnaire questionnaire, Questionnaire targetQuestionnaire) {
    BeanUtils.copyProperties(questionnaire,targetQuestionnaire);
    return targetQuestionnaire;
  }

  private void softDeleteHelper(Questionnaire questionnaire){
    questionnaire.setDeleted(true);
    questionnaireRepository.save(questionnaire);
  }
}
