package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.QuestionQuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireParticipantRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.UserQuestionnaireSummaryRepository;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyQuestionnaireServiceImpl implements MyQuestionnaireService {

  private final QuestionnaireParticipantRepository questionnaireParticipantRepository;

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;

  private final QuestionResponseRepository questionResponseRepository;

  private final QuestionnaireResponseRepository questionnaireResponseRepository;

  private final QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository;

  private final UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

  @Autowired
  public MyQuestionnaireServiceImpl( QuestionnaireParticipantRepository questionnaireParticipantRepository, QuestionQuestionnaireRepository questionQuestionnaireRepository, QuestionResponseRepository questionResponseRepository, QuestionnaireResponseRepository questionnaireResponseRepository, QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository, UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository, QuestionResponseSummaryRepository questionResponseSummaryRepository, UserService userService) {
    this.questionnaireParticipantRepository = questionnaireParticipantRepository;
    this.questionQuestionnaireRepository = questionQuestionnaireRepository;
    this.questionResponseRepository = questionResponseRepository;
    this.questionnaireResponseRepository = questionnaireResponseRepository;
    this.questionnaireResponseSummaryRepository = questionnaireResponseSummaryRepository;
    this.userQuestionnaireSummaryRepository = userQuestionnaireSummaryRepository;
    this.questionResponseSummaryRepository = questionResponseSummaryRepository;
  }

  @Override
  public Page<Questionnaire> getQuestionnairesByMemberLoginAsAppraiser(User memberLogin, Pageable pageable) {
    Page<QuestionnaireParticipant> results = questionnaireParticipantRepository
            .findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(memberLogin, ParticipantType.APPRAISER, pageable);
    List<Questionnaire> questionnaires = new ArrayList<>();
    for(QuestionnaireParticipant result : results){
        questionnaires.add(result.getQuestionnaire());
    }
    return new PageImpl<>(questionnaires, pageable, questionnaires.size());
  }

  @Override
  public List<QuestionnaireParticipant> getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(Questionnaire questionnaire, User memberLogin) {

    Pageable pageable=  new PageRequest(0,10);

    Page<QuestionnaireParticipant> participants =
      questionnaireParticipantRepository
        .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire, ParticipantType.APPRAISEE, pageable);

    List<QuestionnaireParticipant> participantsList  = new ArrayList<>();

    for (QuestionnaireParticipant participant : participants) {
      if(!participant.getMember().getId().equals(memberLogin.getId())) {
        if(! questionnaireResponseRepository
              .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(questionnaire, participant.getMember(), memberLogin).isPresent() ){
          participantsList.add(participant);
        }
      }
    }

    return participantsList;

  }

  @Override
  public QuestionnaireParticipant getQuestionnaireParticipantById(String questionnaireParticipantId) {
    return Optional.of(questionnaireParticipantId)
            .map(questionnaireParticipantRepository::findOne)
            .orElse(null);
  }


  @Override
  public List<QuestionQuestionnaire> getQuestionsFromQuestionnaire(Questionnaire questionnaire) {
    return questionQuestionnaireRepository.findAllByQuestionnaire(questionnaire);
  }

  @Override
  public QuestionnaireResponse
    createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
      Questionnaire questionnaire,
      List<QuestionResponse> questionResponses,
      User memberLogin,
      User appraisee) {

    Answer scoreSummary = Answer.builder()
            .maximum(Float.valueOf(0))
            .minimum(Float.valueOf(6))
            .build();

    Float avarageScore = new Float(0.0);

    for (QuestionResponse questionResponse : questionResponses) {
      questionResponseRepository.save(questionResponse);
      updateQuestionResponseSummary(questionnaire, questionResponse);
      scoreSummary.setMaximum(scoreSummary.getMaximum() < questionResponse.getScore()
                                ? questionResponse.getScore()
                                : scoreSummary.getMaximum()
                              );
      scoreSummary.setMinimum(scoreSummary.getMinimum() > questionResponse.getScore()
                                ? questionResponse.getScore()
                                : scoreSummary.getMinimum()
                              );
      avarageScore += questionResponse.getScore();
    }

    avarageScore = avarageScore/questionResponses.size();
    scoreSummary.setAverage(avarageScore);

    QuestionnaireResponse questionnaireResponse =
      QuestionnaireResponse.builder()
      .questionnaire(questionnaire)
      .details(questionResponses)
      .appraisee(appraisee)
      .appraiser(memberLogin)
      .scoreSummary(scoreSummary)
      .build();

    this.updateQuestionnaireResponseSummary(questionnaireResponse);

    this.updateUserQuestionnaireSummary(questionnaireResponse);

    return questionnaireResponseRepository.save(questionnaireResponse);
  }

  public void updateQuestionResponseSummary(Questionnaire questionnaire, QuestionResponse questionResponse){
    Optional<QuestionResponseSummary> temp =
      questionResponseSummaryRepository.findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(
        questionResponse.getAppraisee(),
        questionResponse.getQuestion());
    QuestionResponseSummary questionResponseSummary = QuestionResponseSummary.builder().build();


    if ( temp.isPresent() ) {
      questionResponseSummary = temp.get();
      Answer tempAnswerSummary = Answer.builder().build();

      float average = questionResponseSummary.getScoreSummary().getAverage();
      int count = questionResponseSummary.getCounter();
      tempAnswerSummary.setMinimum(questionResponseSummary.getScoreSummary().getMinimum() > questionResponse.getScore()
                                    ? questionResponse.getScore() : questionResponseSummary.getScoreSummary().getMinimum() );
      tempAnswerSummary.setMaximum(questionResponseSummary.getScoreSummary().getMaximum() < questionResponse.getScore()
                                    ? questionResponse.getScore() : questionResponseSummary.getScoreSummary().getMaximum() );
      tempAnswerSummary.setAverage(( average * count + questionResponse.getScore() ) / ( count + 1 ) );
      questionResponseSummary.setScoreSummary(tempAnswerSummary);
      questionResponseSummary.setCounter(count + 1);
    } else {
      Answer tempAnswerSummary =
        Answer.builder()
          .average(questionResponse.getScore())
          .maximum(questionResponse.getScore())
          .minimum(questionResponse.getScore())
          .build();

      questionResponseSummary =
        QuestionResponseSummary.builder()
          .questionnaire(questionnaire)
          .question(questionResponse.getQuestion())
          .appraisee(questionResponse.getAppraisee())
          .scoreSummary(tempAnswerSummary)
          .counter(1)
          .build();
    }

    questionResponseSummaryRepository.save(questionResponseSummary);

  }

  public void updateQuestionnaireResponseSummary(QuestionnaireResponse questionnaireResponse) {
    Optional<QuestionnaireResponseSummary> temp = questionnaireResponseSummaryRepository
      .findByAppraiseeAndQuestionnaireAndDeletedFalse(
        questionnaireResponse.getAppraisee(),
        questionnaireResponse.getQuestionnaire()
      );

    QuestionnaireResponseSummary questionnaireResponseSummary = QuestionnaireResponseSummary.builder().build();
    if( temp.isPresent()) {
      questionnaireResponseSummary = temp.get();
      Answer tempAnswerSummary = Answer.builder().build();

      float average = questionnaireResponseSummary.getScoreSummary().getAverage();
      int count = questionnaireResponseSummary.getCounter();
      tempAnswerSummary.setMinimum(
        questionnaireResponseSummary.getScoreSummary().getMinimum() > questionnaireResponse.getScoreSummary().getAverage()
          ? questionnaireResponse.getScoreSummary().getAverage()
          : questionnaireResponseSummary.getScoreSummary().getMinimum() );
      tempAnswerSummary.setMaximum(
        questionnaireResponseSummary.getScoreSummary().getMaximum() < questionnaireResponse.getScoreSummary().getAverage()
          ? questionnaireResponse.getScoreSummary().getAverage()
          : questionnaireResponseSummary.getScoreSummary().getMaximum() );
      tempAnswerSummary.setAverage(( average * count + questionnaireResponse.getScoreSummary().getAverage() ) / ( count + 1 ) );
      questionnaireResponseSummary.setScoreSummary(tempAnswerSummary);
      questionnaireResponseSummary.setCounter(count + 1);
    } else {
      questionnaireResponseSummary = QuestionnaireResponseSummary.builder()
        .questionnaire(questionnaireResponse.getQuestionnaire())
        .appraisee(questionnaireResponse.getAppraisee())
        .scoreSummary(questionnaireResponse.getScoreSummary())
        .counter(1)
        .build();
    }

    questionnaireResponseSummaryRepository.save(questionnaireResponseSummary);
  }

  public void updateUserQuestionnaireSummary(QuestionnaireResponse questionnaireResponse) {
    Optional<UserQuestionnaireSummary> temp = userQuestionnaireSummaryRepository
      .findFirstByAppraiseeAndDeletedFalse(questionnaireResponse.getAppraisee());
    UserQuestionnaireSummary userQuestionnaireSummary = UserQuestionnaireSummary.builder().build();
    if (! temp.isPresent()) {
      userQuestionnaireSummary = UserQuestionnaireSummary.builder()
        .role(questionnaireResponse.getAppraisee().getRole())
        .batch(questionnaireResponse.getAppraisee().getBatch())
        .appraisee(questionnaireResponse.getAppraisee())
        .scoreSummary(questionnaireResponse.getScoreSummary())
        .counter(1)
        .build();
    } else {
      userQuestionnaireSummary = temp.get();

      Answer tempQuestionnaireSummary = Answer.builder().build();

      float average = userQuestionnaireSummary.getScoreSummary().getAverage();
      int count = userQuestionnaireSummary.getCounter();

      tempQuestionnaireSummary.setMinimum(
        userQuestionnaireSummary.getScoreSummary().getMinimum() > questionnaireResponse.getScoreSummary().getAverage()
          ? questionnaireResponse.getScoreSummary().getAverage()
          : userQuestionnaireSummary.getScoreSummary().getMinimum() );
      tempQuestionnaireSummary.setMaximum(
        userQuestionnaireSummary.getScoreSummary().getMaximum() < questionnaireResponse.getScoreSummary().getAverage()
          ? questionnaireResponse.getScoreSummary().getAverage()
          : userQuestionnaireSummary.getScoreSummary().getMaximum() );
      tempQuestionnaireSummary.setAverage(( average * count + questionnaireResponse.getScoreSummary().getAverage() ) / ( count + 1 ) );

      userQuestionnaireSummary.setScoreSummary(tempQuestionnaireSummary);
      userQuestionnaireSummary.setCounter(count+1);
    }
    userQuestionnaireSummaryRepository.save(userQuestionnaireSummary);
  }
}
