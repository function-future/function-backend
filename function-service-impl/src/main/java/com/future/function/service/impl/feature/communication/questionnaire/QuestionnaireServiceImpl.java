package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.QuestionQuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireParticipantRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

  private final QuestionnaireRepository questionnaireRepository;

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;

  private final UserService userService;

  private final QuestionnaireParticipantRepository
    questionnaireParticipantRepository;

  @Autowired
  public QuestionnaireServiceImpl(
    QuestionnaireRepository questionnaireRepository,
    QuestionQuestionnaireRepository questionQuestionnaireRepository,
    UserService userService,
    QuestionnaireParticipantRepository questionnaireParticipantRepository
  ) {

    this.questionnaireRepository = questionnaireRepository;
    this.questionQuestionnaireRepository = questionQuestionnaireRepository;
    this.userService = userService;
    this.questionnaireParticipantRepository =
      questionnaireParticipantRepository;
  }

  @Override
  public Page<Questionnaire> getAllQuestionnaires(Pageable pageable) {

    return questionnaireRepository.findAllByDeletedFalseOrderByCreatedAtDesc(
      pageable);
  }

  @Override
  public Page<Questionnaire> getQuestionnairesWithKeyword(
    String keyword, Pageable pageable
  ) {

    return questionnaireRepository.findAllByTitleIgnoreCaseContainingAndDeletedFalseOrderByCreatedAtDesc(
      keyword, pageable);
  }


  @Override
  public Questionnaire getQuestionnaire(String questionnaireId) {

    return Optional.of(questionnaireId)
      .map(this.questionnaireRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Questionnaire not Found with"));
  }

  @Override
  public Questionnaire createQuestionnaire(
    Questionnaire questionnaire, User author
  ) {

    return Optional.of(questionnaire)
      .map(questionnaire1 -> this.setAuthorHelper(questionnaire, author))
      .map(questionnaireRepository::save)
      .orElseThrow(UnsupportedOperationException::new);
  }

  private Questionnaire setAuthorHelper(
    Questionnaire questionnaire, User author
  ) {

    questionnaire.setAuthor(userService.getUser(author.getId()));
    return questionnaire;
  }

  @Override
  public Questionnaire updateQuestionnaire(Questionnaire questionnaire) {

    return Optional.of(questionnaire)
      .map(Questionnaire::getId)
      .map(questionnaireRepository::findOne)
      .map(temp -> this.copyProperties(questionnaire, temp))
      .map(questionnaireRepository::save)
      .orElse(questionnaire);
  }

  @Override
  public void deleteQuestionnaire(String questionnaireId) {

    Optional.ofNullable(questionnaireId)
      .map(questionnaireRepository::findOne)
      .map(this::softDeleteHelperQuestionnaire)
      .map(
        questionnaireParticipantRepository::findAllByQuestionnaireAndDeletedFalse)
      .ifPresent(questionnaireParticipants -> questionnaireParticipants.forEach(
        this::softDeletedHelperQuestionnaireParticipant));
  }

  @Override
  public List<QuestionQuestionnaire> getQuestionsByIdQuestionnaire(
    String questionnaireId
  ) {

    return Optional.of(questionnaireId)
      .map(this::getQuestionnaire)
      .map(questionQuestionnaireRepository::findAllByQuestionnaire)
      .orElseThrow(
        () -> new NotFoundException("Questions Questionnaire not found"));
  }

  @Override
  public QuestionQuestionnaire getQuestionQuestionnaire(
    String questionQuestionnaireId
  ) {

    return Optional.of(questionQuestionnaireId)
      .map(questionQuestionnaireRepository::findOne)
      .orElseThrow(() -> new NotFoundException(
        "Question with Id" + questionQuestionnaireId + "not found"));
  }

  @Override
  public QuestionQuestionnaire createQuestionQuestionnaire(
    QuestionQuestionnaire questionQuestionnaire
  ) {

    return Optional.of(questionQuestionnaire)
      .map(target -> this.setQuestionnaire(questionQuestionnaire, target))
      .map(questionQuestionnaireRepository::save)
      .orElseThrow(UnsupportedOperationException::new);
  }

  @Override
  public QuestionQuestionnaire updateQuestionQuestionnaire(
    QuestionQuestionnaire questionQuestionnaire
  ) {

    return Optional.of(questionQuestionnaire)
      .map(QuestionQuestionnaire::getId)
      .map(questionQuestionnaireRepository::findOne)
      .map(target -> this.setQuestionnaire(questionQuestionnaire, target))
      .map(target -> this.copyProperties(questionQuestionnaire, target))
      .map(questionQuestionnaireRepository::save)
      .orElse(questionQuestionnaire);
  }

  @Override
  public void deleteQuestionQuestionnaire(String questionQuestionnaireId) {

    Optional.ofNullable(questionQuestionnaireId)
      .map(questionQuestionnaireRepository::findOne)
      .ifPresent(this::softDeletedHelperQuestionQuestionnaire);
  }

  @Override
  public Page<QuestionnaireParticipant> getQuestionnaireAppraiser(
    Questionnaire questionnaire, Pageable pageable
  ) {

    return questionnaireParticipantRepository.findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
      questionnaire, ParticipantType.APPRAISER, pageable);
  }

  @Override
  public QuestionnaireParticipant addQuestionnaireAppraiserToQuestionnaire(
    String questionnaireId, String appraiserId
  ) {

    QuestionnaireParticipant questionnaireParticipant =
      QuestionnaireParticipant.builder()
        .participantType(ParticipantType.APPRAISER)
        .member(userService.getUser(appraiserId))
        .questionnaire(questionnaireRepository.findOne(questionnaireId))
        .build();

    return Optional.of(questionnaireParticipant)
      .map(questionnaireParticipantRepository::save)
      .orElse(null);
  }

  @Override
  public void deleteQuestionnaireAppraiserFromQuestionnaire(
    String questionnaireParticipantId
  ) {

    Optional.ofNullable(questionnaireParticipantId)
      .map(questionnaireParticipantRepository::findOne)
      .ifPresent(this::softDeletedHelperQuestionnaireParticipant);
  }

  @Override
  public Page<QuestionnaireParticipant> getQuestionnaireAppraisee(
    Questionnaire questionnaire, Pageable pageable
  ) {

    return questionnaireParticipantRepository.findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
      questionnaire, ParticipantType.APPRAISEE, pageable);
  }

  @Override
  public QuestionnaireParticipant addQuestionnaireAppraiseeToQuestionnaire(
    String questionnaireId, String appraiseeId
  ) {

    QuestionnaireParticipant questionnaireParticipant =
      QuestionnaireParticipant.builder()
        .participantType(ParticipantType.APPRAISEE)
        .member(userService.getUser(appraiseeId))
        .questionnaire(questionnaireRepository.findOne(questionnaireId))
        .build();

    return Optional.of(questionnaireParticipant)
      .map(questionnaireParticipantRepository::save)
      .orElse(null);
  }

  @Override
  public void deleteQuestionnaireAppraiseeFromQuestionnaire(
    String questionnaireParticipantId
  ) {

    Optional.ofNullable(questionnaireParticipantId)
      .map(questionnaireParticipantRepository::findOne)
      .ifPresent(this::softDeletedHelperQuestionnaireParticipant);
  }

  @Override
  public Boolean validateQuestionnaire(String questionnaireId, Long newStartDate, Long newDueDate) {
    Long serverTime = new Date().getTime();

    if (questionnaireId != null) {
      Questionnaire questionnaire = this.getQuestionnaire(questionnaireId);
      return !(serverTime >= questionnaire.getStartDate() || serverTime >= questionnaire.getDueDate());
    }
    return !(serverTime >= newStartDate || newStartDate >= newDueDate);
  }


  private void softDeletedHelperQuestionQuestionnaire(
    QuestionQuestionnaire questionQuestionnaire
  ) {

    questionQuestionnaire.setDeleted(true);
    questionQuestionnaire.setQuestionnaire(null);
    questionQuestionnaireRepository.save(questionQuestionnaire);
  }

  private QuestionQuestionnaire copyProperties(
    QuestionQuestionnaire questionQuestionnaire,
    QuestionQuestionnaire targetQuestionQuestionnaire
  ) {

    targetQuestionQuestionnaire.setDescription(
      questionQuestionnaire.getDescription());
    return targetQuestionQuestionnaire;
  }

  private QuestionQuestionnaire setQuestionnaire(
    QuestionQuestionnaire questionQuestionnaire,
    QuestionQuestionnaire targetQuestionQuestionnaire
  ) {

    targetQuestionQuestionnaire.setQuestionnaire(this.getQuestionnaire(
      questionQuestionnaire.getQuestionnaire()
        .getId()));
    return targetQuestionQuestionnaire;
  }

  private Questionnaire softDeleteHelperQuestionnaire(
    Questionnaire questionnaire
  ) {

    questionnaire.setDeleted(true);
    return questionnaireRepository.save(questionnaire);
  }

  private void softDeletedHelperQuestionnaireParticipant(
    QuestionnaireParticipant questionnaireParticipant
  ) {

    questionnaireParticipant.setDeleted(true);
    questionnaireParticipant.setMember(null);
    questionnaireParticipant.setParticipantType(ParticipantType.UNKNOWN);
    questionnaireParticipant.setQuestionnaire(null);
    questionnaireParticipantRepository.save(questionnaireParticipant);
  }

  private Questionnaire copyProperties(
    Questionnaire questionnaire, Questionnaire targetQuestionnaire
  ) {

    targetQuestionnaire.setStartDate(questionnaire.getStartDate());
    targetQuestionnaire.setDueDate(questionnaire.getDueDate());
    targetQuestionnaire.setTitle(questionnaire.getTitle());
    targetQuestionnaire.setDescription(questionnaire.getDescription());
    targetQuestionnaire.setAuthor(questionnaire.getAuthor());
    return targetQuestionnaire;
  }

}
