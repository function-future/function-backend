package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.*;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.*;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyQuestionnaireServiceImpl implements MyQuestionnaireService {

  private final QuestionnaireParticipantRepository
    questionnaireParticipantRepository;

  private final QuestionQuestionnaireRepository questionQuestionnaireRepository;

  private final QuestionResponseRepository questionResponseRepository;

  private final QuestionnaireResponseRepository questionnaireResponseRepository;

  private final QuestionnaireResponseSummaryRepository
    questionnaireResponseSummaryRepository;

  private final UserQuestionnaireSummaryRepository
    userQuestionnaireSummaryRepository;

  private final QuestionResponseSummaryRepository
    questionResponseSummaryRepository;

  private final QuestionResponseQueueRepository
    questionResponseQueueRepository;

  @Autowired
  public MyQuestionnaireServiceImpl(
    QuestionnaireParticipantRepository questionnaireParticipantRepository,
    QuestionQuestionnaireRepository questionQuestionnaireRepository,
    QuestionResponseRepository questionResponseRepository,
    QuestionnaireResponseRepository questionnaireResponseRepository,
    QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository,
    UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository,
    QuestionResponseSummaryRepository questionResponseSummaryRepository,
    QuestionResponseQueueRepository questionResponseQueueRepository
  ) {
    this.questionnaireParticipantRepository =
      questionnaireParticipantRepository;
    this.questionQuestionnaireRepository = questionQuestionnaireRepository;
    this.questionResponseRepository = questionResponseRepository;
    this.questionnaireResponseRepository = questionnaireResponseRepository;
    this.questionnaireResponseSummaryRepository =
      questionnaireResponseSummaryRepository;
    this.userQuestionnaireSummaryRepository =
      userQuestionnaireSummaryRepository;
    this.questionResponseSummaryRepository = questionResponseSummaryRepository;
    this.questionResponseQueueRepository = questionResponseQueueRepository;
  }

  @Override
  public Page<Questionnaire> getQuestionnairesByMemberLoginAsAppraiser(
    User memberLogin, Pageable pageable
  ) {
    Page<QuestionnaireParticipant> results =
      questionnaireParticipantRepository.findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
        memberLogin, ParticipantType.APPRAISER, pageable);
    List<Questionnaire> questionnaires = new ArrayList<>();
    for (QuestionnaireParticipant result : results) {
      questionnaires.add(result.getQuestionnaire());
    }
    return new PageImpl<>(questionnaires, pageable, questionnaires.size());
  }

  @Override
  public List<QuestionnaireParticipant> getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(
    Questionnaire questionnaire, User memberLogin
  ) {

    Pageable pageable = new PageRequest(0, 10);

    Page<QuestionnaireParticipant> participants =
      questionnaireParticipantRepository.findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
        questionnaire, ParticipantType.APPRAISEE, pageable);

    List<QuestionnaireParticipant> participantsList = new ArrayList<>();

    for (QuestionnaireParticipant participant : participants) {
      if (!participant.getMember()
        .getId()
        .equals(memberLogin.getId()) &&
          !questionnaireResponseRepository.findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
            questionnaire, participant.getMember(), memberLogin)
            .isPresent()) {
        participantsList.add(participant);
      }
    }

    return participantsList;
  }

  @Override
  public List<QuestionnaireResponse> getListAppraiseeDone(
    Questionnaire questionnaire, User memberLogin
  ) {
    List<QuestionnaireResponse> questionnaireResponses;

    questionnaireResponses =
      questionnaireResponseRepository
        .findAllByQuestionnaireAndAppraiserAndDeletedFalse(questionnaire, memberLogin);

    return questionnaireResponses;
  }

  @Override
  public QuestionnaireParticipant getQuestionnaireParticipantById(
    String questionnaireParticipantId
  ) {

    return Optional.of(questionnaireParticipantId)
      .map(questionnaireParticipantRepository::findOne)
      .orElse(null);
  }


  @Override
  public List<QuestionQuestionnaire> getQuestionsFromQuestionnaire(
    Questionnaire questionnaire
  ) {

    return questionQuestionnaireRepository.findAllByQuestionnaire(
      questionnaire);
  }

  @Override
  public void createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
    Questionnaire questionnaire,
    List<QuestionResponseQueue> questionResponses,
    User memberLogin,
    User appraisee
  ) {
    questionResponseQueueRepository.save(questionResponses);

    if (!questionnaireResponseRepository
          .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
              questionnaire, appraisee, memberLogin)
      .isPresent()) {
      Answer answer = Answer.builder()
        .maximum(0F)
        .minimum(0F)
        .average(0F)
        .build();

      QuestionnaireResponse questionnaireResponse =
        QuestionnaireResponse.builder()
          .questionnaire(questionnaire)
          .appraisee(appraisee)
          .appraiser(memberLogin)
          .scoreSummary(answer)
          .build();
      this.questionnaireResponseRepository.save(questionnaireResponse);
    }
  }

  public void updateUserSummary(
    Questionnaire questionnaire, List<QuestionResponse> questionResponses,
    User memberLogin, User appraisee
  ) {
    Answer scoreSummary = Answer.builder()
      .maximum(0F)
      .minimum(6F)
      .build();

    Float avarageScore = new Float(0.0);

//    System.out.println(questionResponses.size());
    for (QuestionResponse questionResponse : questionResponses) {
//      System.out.println(questionResponse);
      this.questionResponseRepository.save(questionResponse);
      updateQuestionResponseSummary(questionnaire, questionResponse);
      scoreSummary.setMaximum(
        scoreSummary.getMaximum() < questionResponse.getScore()
          ? questionResponse.getScore() : scoreSummary.getMaximum());
      scoreSummary.setMinimum(
        scoreSummary.getMinimum() > questionResponse.getScore()
          ? questionResponse.getScore() : scoreSummary.getMinimum());
      avarageScore += questionResponse.getScore();
    }

    avarageScore = avarageScore / questionResponses.size();
    scoreSummary.setAverage(avarageScore);

    QuestionnaireResponse questionnaireResponse = questionnaireResponseRepository
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire, appraisee, memberLogin
      ).get();

    questionnaireResponse.setDetails(questionResponses);
    questionnaireResponse.setScoreSummary(scoreSummary);

    this.updateQuestionnaireResponseSummary(questionnaireResponse);

    this.updateUserQuestionnaireSummary(questionnaireResponse);

    this.questionnaireResponseRepository.save(questionnaireResponse);
  }

  @Scheduled(fixedDelayString = "#{@questionnaireProperties.updateUserSummariesPeriod}")
  public void updateScore() {

    Authentication auth = new UsernamePasswordAuthenticationToken(
      "system", "system");
    SecurityContextHolder.getContext()
      .setAuthentication(auth);

    List<QuestionResponseQueue> questionResponseQueues =
      this.questionResponseQueueRepository.findAll();

    if (!questionResponseQueues.isEmpty()) {
      QuestionResponse questionResponse = toQuestionResponse(questionResponseQueues.get(0));
      Questionnaire questionnaireTemp = questionResponse.getQuestion().getQuestionnaire();
      User appraiseeTemp = questionResponse.getAppraisee();
      User appraiserTemp = questionResponse.getAppraiser();
      List<QuestionResponse> questionResponses = new ArrayList<QuestionResponse>();

      for (QuestionResponseQueue q: questionResponseQueues) {
        if (questionnaireTemp.getId().equals(q.getQuestion().getQuestionnaire().getId()) &&
          appraiseeTemp.getId().equals(q.getAppraisee().getId()) &&
          appraiserTemp.getId().equals(q.getAppraiser().getId())
        ) {
          questionResponses.add(toQuestionResponse(q));
        } else {
          this.updateUserSummary(
            questionnaireTemp,
            questionResponses,
            appraiserTemp,
            appraiseeTemp
          );
          questionnaireTemp = q.getQuestion().getQuestionnaire();
          appraiseeTemp = q.getAppraisee();
          appraiserTemp = q.getAppraiser();
          questionResponses.clear();
          questionResponses.add(toQuestionResponse(q));
        }
      }
      if (!questionResponses.isEmpty()) {
        this.updateUserSummary(
          questionnaireTemp,
          questionResponses,
          appraiserTemp,
          appraiseeTemp
        );
      }
      questionResponseQueueRepository.delete(questionResponseQueues);
    }
  }

  public void updateQuestionResponseSummary(
    Questionnaire questionnaire, QuestionResponse questionResponse
  ) {

    Optional<QuestionResponseSummary> temp =
      questionResponseSummaryRepository.findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(
        questionResponse.getAppraisee(), questionResponse.getQuestion());
    QuestionResponseSummary questionResponseSummary;


    if (temp.isPresent()) {
      questionResponseSummary = temp.get();
      Answer tempAnswerSummary = Answer.builder()
        .build();

      float average = questionResponseSummary.getScoreSummary()
        .getAverage();
      int count = questionResponseSummary.getCounter();
      tempAnswerSummary.setMinimum(questionResponseSummary.getScoreSummary()
                                     .getMinimum() > questionResponse.getScore()
                                   ? questionResponse.getScore()
                                   : questionResponseSummary.getScoreSummary()
                                     .getMinimum());
      tempAnswerSummary.setMaximum(questionResponseSummary.getScoreSummary()
                                     .getMaximum() < questionResponse.getScore()
                                   ? questionResponse.getScore()
                                   : questionResponseSummary.getScoreSummary()
                                     .getMaximum());
      tempAnswerSummary.setAverage(
        (average * count + questionResponse.getScore()) / (count + 1));
      questionResponseSummary.setScoreSummary(tempAnswerSummary);
      questionResponseSummary.setCounter(count + 1);
    } else {
      Answer tempAnswerSummary = Answer.builder()
        .average(questionResponse.getScore())
        .maximum(questionResponse.getScore())
        .minimum(questionResponse.getScore())
        .build();

      questionResponseSummary = QuestionResponseSummary.builder()
        .questionnaire(questionnaire)
        .question(questionResponse.getQuestion())
        .appraisee(questionResponse.getAppraisee())
        .scoreSummary(tempAnswerSummary)
        .counter(1)
        .build();
    }

    questionResponseSummaryRepository.save(questionResponseSummary);
  }

  public void updateQuestionnaireResponseSummary(
    QuestionnaireResponse questionnaireResponse
  ) {
    Optional<QuestionnaireResponseSummary> temp =
      questionnaireResponseSummaryRepository.findByAppraiseeAndQuestionnaireAndDeletedFalse(
        questionnaireResponse.getAppraisee(),
        questionnaireResponse.getQuestionnaire());
    QuestionnaireResponseSummary questionnaireResponseSummary;
    if (temp.isPresent()) {
      questionnaireResponseSummary = temp.get();
      Answer tempAnswerSummary = Answer.builder()
        .build();
      float average = questionnaireResponseSummary.getScoreSummary()
        .getAverage();
      int count = questionnaireResponseSummary.getCounter();
      tempAnswerSummary.setMinimum(
        questionnaireResponseSummary.getScoreSummary()
          .getMinimum() > questionnaireResponse.getScoreSummary()
          .getAverage() ? questionnaireResponse.getScoreSummary()
          .getAverage() : questionnaireResponseSummary.getScoreSummary().getMinimum());
      tempAnswerSummary.setMaximum(
        questionnaireResponseSummary.getScoreSummary()
          .getMaximum() < questionnaireResponse.getScoreSummary()
          .getAverage() ? questionnaireResponse.getScoreSummary()
          .getAverage() : questionnaireResponseSummary.getScoreSummary().getMaximum());
      tempAnswerSummary.setAverage((
                                     average * count +
                                     questionnaireResponse.getScoreSummary()
                                       .getAverage()
                                   ) / (count + 1));
      questionnaireResponseSummary.setScoreSummary(tempAnswerSummary);
      questionnaireResponseSummary.setCounter(count + 1);
    } else {
      Answer tempAnswerSummary = Answer.builder()
        .minimum(questionnaireResponse.getScoreSummary().getMinimum())
        .average(questionnaireResponse.getScoreSummary().getAverage())
        .maximum(questionnaireResponse.getScoreSummary().getMaximum())
        .build();

      questionnaireResponseSummary = QuestionnaireResponseSummary.builder()
        .scoreSummary(tempAnswerSummary)
        .appraisee(questionnaireResponse.getAppraisee())
        .questionnaire(questionnaireResponse.getQuestionnaire())
        .counter(1)
        .build();
    }
    questionnaireResponseSummaryRepository.save(questionnaireResponseSummary);
  }

  public void updateUserQuestionnaireSummary(
    QuestionnaireResponse questionnaireResponse
  ) {

    Optional<UserQuestionnaireSummary> temp =
      userQuestionnaireSummaryRepository.findFirstByAppraiseeAndDeletedFalse(
        questionnaireResponse.getAppraisee());
    UserQuestionnaireSummary userQuestionnaireSummary;
    if (!temp.isPresent()) {
      userQuestionnaireSummary = UserQuestionnaireSummary.builder()
        .role(questionnaireResponse.getAppraisee()
                .getRole())
        .batch(questionnaireResponse.getAppraisee()
                 .getBatch())
        .appraisee(questionnaireResponse.getAppraisee())
        .scoreSummary(questionnaireResponse.getScoreSummary())
        .counter(1)
        .build();
    } else {
      userQuestionnaireSummary = temp.get();

      Answer tempQuestionnaireSummary = Answer.builder()
        .build();
      float average = userQuestionnaireSummary.getScoreSummary()
        .getAverage();
      int count = userQuestionnaireSummary.getCounter();
      tempQuestionnaireSummary.setMinimum(
        userQuestionnaireSummary.getScoreSummary()
          .getMinimum() > questionnaireResponse.getScoreSummary()
          .getAverage() ? questionnaireResponse.getScoreSummary()
          .getAverage() : userQuestionnaireSummary.getScoreSummary().getMinimum());
      tempQuestionnaireSummary.setMaximum(
        userQuestionnaireSummary.getScoreSummary()
          .getMaximum() < questionnaireResponse.getScoreSummary()
          .getAverage() ? questionnaireResponse.getScoreSummary()
          .getAverage() : userQuestionnaireSummary.getScoreSummary().getMaximum());
      tempQuestionnaireSummary.setAverage((
                                            average * count +
                                            questionnaireResponse.getScoreSummary()
                                              .getAverage()
                                          ) / (count + 1));
      userQuestionnaireSummary.setScoreSummary(tempQuestionnaireSummary);
      userQuestionnaireSummary.setCounter(count + 1);
    }
    userQuestionnaireSummaryRepository.save(userQuestionnaireSummary);
  }

  public QuestionResponse toQuestionResponse(QuestionResponseQueue questionResponseQueue){
    return
      QuestionResponse.builder()
        .appraisee(questionResponseQueue.getAppraisee())
        .appraiser(questionResponseQueue.getAppraiser())
        .comment(questionResponseQueue.getComment())
        .score(questionResponseQueue.getScore())
        .question(questionResponseQueue.getQuestion())
        .build();
  }
}
