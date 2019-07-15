package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.*;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.*;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
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

  private final QuestionnaireRepository questionnaireRepository;

  private final QuestionnaireParticipantRepository questionnaireParticipantRepository;

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;

  private final QuestionResponseRepository questionResponseRepository;

  private final QuestionnaireResponseRepository questionnaireResponseRepository;

  private final QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository;

  private final UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

  @Autowired
  public MyQuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository, QuestionnaireParticipantRepository questionnaireParticipantRepository, QuestionQuestionnaireRepository questionQuestionnaireRepository, QuestionResponseRepository questionResponseRepository, QuestionnaireResponseRepository questionnaireResponseRepository, QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository, UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository, QuestionResponseSummaryRepository questionResponseSummaryRepository) {
    this.questionnaireRepository = questionnaireRepository;
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
            .findAllByMemberAndParticipantTypeAndDeletedFalse(memberLogin, ParticipantType.APPRAISER, pageable);
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
        participantsList.add(participant);
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

    Float avarageScore = new Float(0.0) ;

    for (QuestionResponse questionResponse : questionResponses) {
      questionResponseRepository.save(questionResponse);
      updateQuestionResponseSummary(questionnaire, questionResponse);
      scoreSummary.setMaximum(scoreSummary.getMaximum() < questionResponse.getScore() ? questionResponse.getScore() : scoreSummary.getMaximum());
      scoreSummary.setMinimum(scoreSummary.getMinimum() > questionResponse.getScore() ? questionResponse.getScore() : scoreSummary.getMinimum());
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

    QuestionnaireResponseSummary questionnaireResponseSummary = updateQuestionnaireResponseSummary(questionnaireResponse);

    this.updateUserQuestionnaireSummary(questionnaireResponseSummary);

    return questionnaireResponse;
  }

  public void updateQuestionResponseSummary(Questionnaire questionnaire, QuestionResponse questionResponse){
    QuestionResponseSummary questionResponseSummary =
            questionResponseSummaryRepository
                    .findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(questionResponse.getAppraisee(), questionResponse.getQuestion()).get();

    if(questionResponseSummary == null) {
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
    } else {
      Answer tempAnswerSummary =
        Answer.builder()
        .average(questionResponse.getScore())
        .maximum(questionResponse.getScore())
        .minimum(questionResponse.getScore())
        .build();

      float average = questionResponseSummary.getScoreSummary().getAverage();
      int count = questionResponseSummary.getCounter();
      tempAnswerSummary.setAverage(( average * count + questionResponse.getScore() ) / count + 1 );
      tempAnswerSummary.setMinimum(average > questionResponse.getScore() ? questionResponse.getScore() : average );
      tempAnswerSummary.setMaximum(average < questionResponse.getScore() ? questionResponse.getScore() : average );
      questionResponseSummary.setScoreSummary(tempAnswerSummary);
      questionResponseSummary.setCounter(count + 1);

    }
    questionResponseSummaryRepository.save(questionResponseSummary);

  }

  public QuestionnaireResponseSummary updateQuestionnaireResponseSummary(QuestionnaireResponse questionnaireResponse) {
    QuestionnaireResponseSummary questionnaireResponseSummary =
      questionnaireResponseSummaryRepository
        .findByAppraiseeAndQuestionnaireAndDeletedFalse(
                questionnaireResponse.getAppraisee(),
                questionnaireResponse.getQuestionnaire()
        )
      .get();

    if (questionnaireResponseSummary == null) {
      questionnaireResponseSummary = QuestionnaireResponseSummary.builder()
              .questionnaire(questionnaireResponse.getQuestionnaire())
              .appraisee(questionnaireResponse.getAppraisee())
              .scoreSummary(questionnaireResponse.getScoreSummary())
              .counter(1)
              .build();
    } else {
      Answer tempAnswerSummary = Answer
              .builder()
              .average(0)
              .maximum(0)
              .minimum(0)
              .build();

      float average = questionnaireResponseSummary.getScoreSummary().getAverage();
      int count = questionnaireResponseSummary.getCounter();
      tempAnswerSummary.setAverage(( average * count + questionnaireResponse.getScoreSummary().getAverage() ) / count + 1 );
      tempAnswerSummary.setMinimum(average > questionnaireResponse.getScoreSummary().getAverage() ? questionnaireResponse.getScoreSummary().getAverage() : average );
      tempAnswerSummary.setMaximum(average < questionnaireResponse.getScoreSummary().getAverage() ? questionnaireResponse.getScoreSummary().getAverage() : average );
      questionnaireResponseSummary.setScoreSummary(tempAnswerSummary);
      questionnaireResponseSummary.setCounter(count + 1);
    }

    return questionnaireResponseSummaryRepository.save(questionnaireResponseSummary);
  }

  public void updateUserQuestionnaireSummary(QuestionnaireResponseSummary questionnaireResponseSummary) {
    UserQuestionnaireSummary userQuestionnaireSummary =
            userQuestionnaireSummaryRepository
                    .findFirstByAppraiseeAndDeletedFalse(questionnaireResponseSummary.getAppraisee()).get();

    Answer tempAnswerSummary = Answer
            .builder()
            .average(0)
            .maximum(0)
            .minimum(0)
            .build();

    float average = userQuestionnaireSummary.getScoreSummary().getAverage();
    int count = userQuestionnaireSummary.getCounter();

    tempAnswerSummary.setAverage(( average * count + questionnaireResponseSummary.getScoreSummary().getAverage() ) / count + 1 );
    tempAnswerSummary.setMinimum(average > questionnaireResponseSummary.getScoreSummary().getAverage() ? questionnaireResponseSummary.getScoreSummary().getAverage() : average );
    tempAnswerSummary.setMaximum(average < questionnaireResponseSummary.getScoreSummary().getAverage() ? questionnaireResponseSummary.getScoreSummary().getAverage() : average );

    questionnaireResponseSummary.setScoreSummary(tempAnswerSummary);
    questionnaireResponseSummary.setCounter(count+1);

    userQuestionnaireSummaryRepository.save(userQuestionnaireSummary);
  }
}
