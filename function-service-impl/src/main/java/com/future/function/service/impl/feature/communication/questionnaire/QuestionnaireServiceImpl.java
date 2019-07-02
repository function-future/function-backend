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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

  private final QuestionnaireRepository questionnaireRepository;

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;


  private final UserService userService;

  private final QuestionnaireParticipantRepository questionnaireParticipantRepository;

  @Autowired
  public QuestionnaireServiceImpl(
          QuestionnaireRepository questionnaireRepository,
          QuestionQuestionnaireRepository questionQuestionnaireRepository,
          UserService userService,
          QuestionnaireParticipantRepository questionnaireParticipantRepository) {
      this.questionnaireRepository = questionnaireRepository;
      this.questionQuestionnaireRepository = questionQuestionnaireRepository;
      this.userService = userService;
      this.questionnaireParticipantRepository = questionnaireParticipantRepository;
  }

  @Override
  public Page<Questionnaire> getAllQuestionnaires(Pageable pageable) {
    return questionnaireRepository.findAll(pageable);
  }

  @Override
  public Page<Questionnaire> getQuestionnairesWithKeyword(String keyword, Pageable pageable) {
    return questionnaireRepository.findAllByTitleIgnoreCaseContainingAndDeletedFalse(keyword, pageable);
  }

  @Override
  public Page<Questionnaire> getQuestionnairesBelongToAppraisee(String appraiseeId, Pageable pageable) {
    return null;
  }

  @Override
  public Questionnaire getQuestionnaire(String questionnaireId) {
    return Optional.of(questionnaireId)
            .map(this.questionnaireRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Questionnaire not Found"));
  }
  @Override
  public Questionnaire createQuestionnaire(Questionnaire questionnaire, User author) {
    return Optional.of(questionnaire)
            .map(questionnaire1 -> this.setAuthorHelper(questionnaire, author))
            .map(questionnaireRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  private Questionnaire setAuthorHelper(Questionnaire questionnaire, User author) {
    questionnaire.setAuthor(userService.getUser(author.getId()));
    return questionnaire;
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
      .ifPresent(this::softDeleteHelperQuestionnaire);
  }

  @Override
  public List<QuestionQuestionnaire> getQuestionsByIdQuestionnaire(String questionnaireId) {
    return Optional.of(questionnaireId)
            .map(this::getQuestionnaire)
            .map(questionQuestionnaireRepository::findAllByQuestionnaire)
            .orElseThrow(() -> new NotFoundException("Questions Questionnaire not found"));
  }

  @Override
  public QuestionQuestionnaire getQuestionQuestionnaire(String questionQuestionnaireId){
    return Optional.of(questionQuestionnaireId)
            .map(questionQuestionnaireRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Question with Id" + questionQuestionnaireId + "not found"));

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
    return Optional.of(questionQuestionnaire)
            .map(QuestionQuestionnaire::getId)
            .map(questionQuestionnaireRepository::findOne)
            .map(target -> this.setQuestionnaire(questionQuestionnaire, target))
            .map(target -> this.copyProperties(questionQuestionnaire,target))
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
  public Page<QuestionnaireParticipant> getQuestionnaireAppraiser(Questionnaire questionnaire, Pageable pageable) {
    return questionnaireParticipantRepository.findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire, ParticipantType.APPRAISER, pageable);
  }
  @Override
  public QuestionnaireParticipant addQuestionnaireAppraiserToQuestionnaire(String questionnaireId, String appraiserId){

    QuestionnaireParticipant questionnaireParticipant = new QuestionnaireParticipant().builder()
            .participantType(ParticipantType.APPRAISER)
            .member(userService.getUser(appraiserId))
            .questionnaire(questionnaireRepository.findOne(questionnaireId))
            .build();

    return Optional.of(questionnaireParticipant)
            .map(questionnaireParticipantRepository::save)
            .orElse(null);
//    return Optional.of(questionnaireParticipant)
//            .map(q ->
//                  questionnaireParticipantRepository.findByQuestionnaireAndMemberAndParticipantTypeAndDeletedFalse(
//                          q.getQuestionnaire(),
//                          q.getMember(),
//                          q.getParticipantType())
//                  .get()
//            )
//            .orElse(questionnaireParticipantRepository.save(questionnaireParticipant))
//            .map(questionnaireParticipantRepository::save)
//            .orElse(null);
  }

  @Override
  public void deleteQuestionnaireAppraiserFromQuestionnaire(String questionnaireParticipantId){
    Optional.ofNullable(questionnaireParticipantId)
            .map(questionQuestionnaireRepository::findOne)
            .ifPresent(this::softDeletedHelperQuestionQuestionnaire);
  }

  @Override
  public Page<QuestionnaireParticipant> getQuestionnaireAppraisee(Questionnaire questionnaire, Pageable pageable) {
    return questionnaireParticipantRepository.findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire,ParticipantType.APPRAISEE ,pageable);
  }

  @Override
  public QuestionnaireParticipant addQuestionnaireAppraiseeToQuestionnaire(String questionnaireId, String appraiseeId){
    QuestionnaireParticipant questionnaireParticipant = new QuestionnaireParticipant().builder()
          .participantType(ParticipantType.APPRAISEE)
          .member(userService.getUser(appraiseeId))
          .questionnaire(questionnaireRepository.findOne(questionnaireId))
          .build();

    return Optional.of(questionnaireParticipant)
            .map(questionnaireParticipantRepository::save)
            .orElse(null);
  }

  @Override
  public void deleteQuestionnaireAppraiseeFromQuestionnaire(String questionnaireParticipantId){
    Optional.ofNullable(questionnaireParticipantId)
            .map(questionnaireParticipantRepository::findOne)
            .ifPresent(this::softDeletedHelperQuestionnaireParticipant);
  }


  private Questionnaire copyProperties(Questionnaire questionnaire, Questionnaire targetQuestionnaire) {
    BeanUtils.copyProperties(questionnaire,targetQuestionnaire);
    return targetQuestionnaire;
  }

  private void softDeleteHelperQuestionnaire(Questionnaire questionnaire){
    questionnaire.setDeleted(true);
    questionnaireRepository.save(questionnaire);
  }

  private QuestionQuestionnaire setQuestionnaire (QuestionQuestionnaire questionQuestionnaire, QuestionQuestionnaire targetQuestionQuestionnaire) {
    targetQuestionQuestionnaire.setQuestionnaire(
            this.getQuestionnaire(questionQuestionnaire.getQuestionnaire().getId())
    );
    return targetQuestionQuestionnaire;
  }

  private QuestionQuestionnaire copyProperties (QuestionQuestionnaire questionQuestionnaire, QuestionQuestionnaire targetQuestionQuestionnaire) {
    BeanUtils.copyProperties(questionQuestionnaire, targetQuestionQuestionnaire);
    return targetQuestionQuestionnaire;
  }

  private void softDeletedHelperQuestionQuestionnaire(QuestionQuestionnaire questionQuestionnaire) {
    questionQuestionnaire.setDeleted(true);
    questionQuestionnaire.setQuestionnaire(null);
    questionQuestionnaireRepository.save(questionQuestionnaire);
  }

  private void softDeletedHelperQuestionnaireParticipant(QuestionnaireParticipant questionnaireParticipant) {
    questionnaireParticipant.setDeleted(true);
    questionnaireParticipant.setMember(null);
    questionnaireParticipant.setParticipantType(ParticipantType.UNKNOWN);
    questionnaireParticipant.setQuestionnaire(null);
    questionnaireParticipantRepository.save(questionnaireParticipant);
  }

  private QuestionnaireParticipant verifyQuestionnaireParticipant(String questionnaireId, String appraiserId, ParticipantType participantType)
    throws Exception
  {
    Optional<QuestionnaireParticipant> questionnaireParticipantTemp =
            questionnaireParticipantRepository.findByQuestionnaireAndMemberAndParticipantTypeAndDeletedFalse(
                    this.getQuestionnaire(questionnaireId), userService.getUser(appraiserId), participantType
            );
    if (questionnaireParticipantTemp.get() == null){
      throw new Exception("error user have been paticipant");
    }
    return questionnaireParticipantTemp.get();
  }

}
