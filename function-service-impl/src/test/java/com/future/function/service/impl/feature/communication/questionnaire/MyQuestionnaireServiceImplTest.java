package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.*;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MyQuestionnaireServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String USER_ID_1 = "userId1";

  private static final String USER_ID_2 = "userId2";

  private static final String USER_LOGGED_IN_ID_1 = "userLoggedInId1";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String TITLE_QUESTIONNAIRE = "titlequestionnaire";

  private static final String QUESTIONNAIRE_RESPONSE_ID_1 =
    "questionnaireResponseId1";

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1 =
    "questionnaireResponseSummaryId1";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_1 =
    "questionnaireParticipantId1";

  private static final String QUESTION_ID_1 = "questionId1";

  private static final String QUESTION_RESPONSE_ID_1 = "questionResponseId1";

  private static final String QUESTION_RESPONSE_QUEUE_ID = "questionResponseQueueId";

  private static final String QUESTION_RESPONSE_QUEUE_ID2 = "questionResponseQueueId2";

  private static final String QUESTION_RESPONSE_SUMMARY_ID_1 =
    "questionResponseSummaryId1";

  private static final float SCORE = (float) 5.0;

  private static final String USER_QUESTIONNAIRE_SUMMARY_ID_1 =
    "userQuestionnaireSummaryId1";

  private static final String COMMENT = "comment";

  private QuestionnaireParticipant questionnaireParticipant1;

  private User user1;

  private User user2;

  private User memberLoggedIn;

  private Questionnaire questionnaire1;

  private QuestionQuestionnaire question1;

  private QuestionResponse questionResponse1;

  private QuestionResponseSummary questionResponseSummary1;

  private QuestionnaireResponse questionnaireResponse1;

  private QuestionnaireResponseSummary questionnaireResponseSummary;

  private UserQuestionnaireSummary userQuestionnaireSummary;

  private Answer answer;

  private Answer answerUpdated;

  private QuestionResponseQueue questionResponseQueue;

  private QuestionResponseQueue questionResponseQueue2;

  @Mock
  private QuestionnaireParticipantRepository questionnaireParticipantRepository;

  @Mock
  private QuestionQuestionnaireRepository questionQuestionnaireRepository;

  @Mock
  private QuestionResponseRepository questionResponseRepository;

  @Mock
  private QuestionnaireResponseRepository questionnaireResponseRepository;

  @Mock
  private QuestionnaireResponseSummaryRepository
    questionnaireResponseSummaryRepository;

  @Mock
  private UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  @Mock
  private QuestionResponseSummaryRepository questionResponseSummaryRepository;

  @Mock
  private QuestionResponseQueueRepository questionResponseQueueRepository;

  @Spy
  @InjectMocks
  private MyQuestionnaireServiceImpl myQuestionnaireService;

  @Before
  public void setUp() {

    user1 = User.builder()
      .id(USER_ID_1)
      .build();

    user2 = User.builder()
      .id(USER_ID_2)
      .build();

    memberLoggedIn = User.builder()
      .id(USER_LOGGED_IN_ID_1)
      .build();

    questionnaire1 = Questionnaire.builder()
      .id(QUESTIONNAIRE_ID_1)
      .title(TITLE_QUESTIONNAIRE)
      .build();

    questionnaireParticipant1 = QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_1)
      .member(user1)
      .questionnaire(questionnaire1)
      .build();

    question1 = QuestionQuestionnaire.builder()
      .id(QUESTION_ID_1)
      .questionnaire(questionnaire1)
      .build();

    questionResponse1 = QuestionResponse.builder()
      .id(QUESTION_RESPONSE_ID_1)
      .question(question1)
      .score(SCORE)
      .comment(COMMENT)
      .appraisee(user1)
      .appraiser(memberLoggedIn)
      .build();

    questionResponseQueue = QuestionResponseQueue.builder()
      .id(QUESTION_RESPONSE_QUEUE_ID)
      .question(question1)
      .score(SCORE)
      .comment(COMMENT)
      .appraisee(user1)
      .appraiser(memberLoggedIn)
      .build();

    questionResponseQueue2 = QuestionResponseQueue.builder()
      .id(QUESTION_RESPONSE_QUEUE_ID2)
      .question(question1)
      .score(SCORE)
      .appraisee(user2)
      .appraiser(memberLoggedIn)
      .build();

    answer = Answer.builder()
      .average(3)
      .maximum(0)
      .minimum(6)
      .build();

    answerUpdated = Answer.builder()
      .average(5)
      .maximum(5)
      .minimum(5)
      .build();

    questionResponseSummary1 = QuestionResponseSummary.builder()
      .id(QUESTION_RESPONSE_SUMMARY_ID_1)
      .counter(1)
      .scoreSummary(answer)
      .build();

    questionnaireResponse1 = QuestionnaireResponse.builder()
      .id(QUESTIONNAIRE_RESPONSE_ID_1)
      .questionnaire(questionnaire1)
      .appraisee(user1)
      .appraiser(memberLoggedIn)
      .build();

    questionnaireResponseSummary = QuestionnaireResponseSummary.builder()
      .id(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1)
      .counter(1)
      .scoreSummary(answer)
      .build();

    userQuestionnaireSummary = UserQuestionnaireSummary.builder()
      .id(USER_QUESTIONNAIRE_SUMMARY_ID_1)
      .scoreSummary(answer)
      .build();
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(questionnaireParticipantRepository,
                             questionQuestionnaireRepository,
                             questionResponseRepository,
                             questionnaireResponseRepository,
                             questionnaireResponseSummaryRepository,
                             userQuestionnaireSummaryRepository,
                             questionResponseSummaryRepository,
                             questionResponseQueueRepository
    );
  }

  @Test
  public void getQuestionnairesByMemberLoginAsAppraiser() {

    when(
      questionnaireParticipantRepository.findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
        user1, ParticipantType.APPRAISER, PAGEABLE)).thenReturn(
      new PageImpl<>(Collections.singletonList(questionnaireParticipant1),
                     PAGEABLE, 1
      ));

    Page<Questionnaire> questionnairePage =
      myQuestionnaireService.getQuestionnairesByMemberLoginAsAppraiser(
        user1,null, PAGEABLE);

    assertThat(questionnairePage.getTotalElements()).isEqualTo(1);
    assertThat(questionnairePage.getContent()
                 .get(0)
                 .getId()).isEqualTo(QUESTIONNAIRE_ID_1);

    verify(
      questionnaireParticipantRepository).findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
      user1, ParticipantType.APPRAISER, PAGEABLE);
  }

  @Test
  public void getQuestionnairesByMemberLoginAsAppraiserWithKeyword() {

    when(
      questionnaireParticipantRepository.findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
        user1, ParticipantType.APPRAISER, PAGEABLE)).thenReturn(
      new PageImpl<>(Collections.singletonList(questionnaireParticipant1),
        PAGEABLE, 1
      ));

    when(
      questionnaireParticipantRepository.findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
        user1, ParticipantType.APPRAISER, new PageRequest(1, 10))).thenReturn(
      new PageImpl<>(Collections.singletonList(questionnaireParticipant1),
        PAGEABLE, 1
      ));

    Page<Questionnaire> questionnairePage =
      myQuestionnaireService.getQuestionnairesByMemberLoginAsAppraiser(
        user1,TITLE_QUESTIONNAIRE, PAGEABLE);

    assertThat(questionnairePage.getTotalElements()).isEqualTo(2);
    assertThat(questionnairePage.getContent()
      .get(0)
      .getId()).isEqualTo(QUESTIONNAIRE_ID_1);

    verify(
      questionnaireParticipantRepository).findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
      user1, ParticipantType.APPRAISER, PAGEABLE);

    verify(
      questionnaireParticipantRepository).findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
      user1, ParticipantType.APPRAISER, new PageRequest(1, 10));
  }

  @Test
  public void getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser() {

    when(
      questionnaireParticipantRepository.findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
        questionnaire1, ParticipantType.APPRAISEE, PAGEABLE)).thenReturn(
      new PageImpl<>(Collections.singletonList(questionnaireParticipant1),
                     PAGEABLE, 1
      ));

    when(
      questionnaireResponseRepository.findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire1, user1, memberLoggedIn)).thenReturn(Optional.empty());

    List<QuestionnaireParticipant> questionnaireParticipants =
      myQuestionnaireService.getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(
        questionnaire1, memberLoggedIn);

    assertThat(questionnaireParticipants.size()).isEqualTo(1);

    verify(
      questionnaireParticipantRepository).findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
      questionnaire1, ParticipantType.APPRAISEE, PAGEABLE);

    verify(
      questionnaireResponseRepository).findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
      questionnaire1, user1, memberLoggedIn);
  }

  @Test
  public void getQuestionnaireParticipantById() {

    when(questionnaireParticipantRepository.findOne(
      QUESTIONNAIRE_PARTICIPANT_ID_1)).thenReturn(questionnaireParticipant1);

    QuestionnaireParticipant questionnaireParticipant =
      myQuestionnaireService.getQuestionnaireParticipantById(
        QUESTIONNAIRE_PARTICIPANT_ID_1);

    assertThat(questionnaireParticipant.getId()).isEqualTo(
      QUESTIONNAIRE_PARTICIPANT_ID_1);

    verify(questionnaireParticipantRepository).findOne(
      QUESTIONNAIRE_PARTICIPANT_ID_1);
  }

  @Test
  public void getQuestionsFromQuestionnaire() {

    when(questionQuestionnaireRepository.findAllByQuestionnaire(
      questionnaire1)).thenReturn(Arrays.asList(question1));

    List<QuestionQuestionnaire> questions =
      myQuestionnaireService.getQuestionsFromQuestionnaire(questionnaire1);

    assertThat(questions.size()).isEqualTo(1);
    assertThat(questions.get(0)
                 .getId()).isEqualTo(QUESTION_ID_1);

    verify(questionQuestionnaireRepository).findAllByQuestionnaire(
      questionnaire1);
  }

  @Test
  public void updateUserSummary() {

    when(questionResponseRepository.save(questionResponse1)).thenReturn(
      questionResponse1);

    when(
      questionResponseSummaryRepository.findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(
        user1, question1)).thenReturn(Optional.of(questionResponseSummary1));

    when(
      questionnaireResponseSummaryRepository.findByAppraiseeAndQuestionnaireAndDeletedFalse(
        user1, questionnaire1)).thenReturn(
      Optional.of(questionnaireResponseSummary));

    when(userQuestionnaireSummaryRepository.findFirstByAppraiseeAndDeletedFalse(
      user1)).thenReturn(Optional.of(userQuestionnaireSummary));

    when(questionResponseSummaryRepository.save(
      questionResponseSummary1)).thenReturn(questionResponseSummary1);

    when(questionnaireResponseRepository.save(
      any(QuestionnaireResponse.class))).thenReturn(questionnaireResponse1);

    when(questionnaireResponseRepository
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire1, user1, memberLoggedIn
      )).thenReturn(Optional.ofNullable(questionnaireResponse1));

    when(questionnaireResponseSummaryRepository.save(
      questionnaireResponseSummary)).thenReturn(questionnaireResponseSummary);

    when(userQuestionnaireSummaryRepository.save(
      userQuestionnaireSummary)).thenReturn(userQuestionnaireSummary);


    myQuestionnaireService.updateUserSummary(
        questionnaire1, Arrays.asList(questionResponse1), memberLoggedIn,
        user1
      );

    verify(questionResponseRepository).save(questionResponse1);

    verify(
      questionResponseSummaryRepository).findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(
      user1, question1);

    verify(
      questionnaireResponseSummaryRepository).findByAppraiseeAndQuestionnaireAndDeletedFalse(
      user1, questionnaire1);

    verify(
      userQuestionnaireSummaryRepository).findFirstByAppraiseeAndDeletedFalse(
      user1);

    verify(questionResponseSummaryRepository).save(questionResponseSummary1);

    verify(questionnaireResponseRepository).save(
      any(QuestionnaireResponse.class));

    verify(questionnaireResponseRepository)
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire1, user1, memberLoggedIn
      );

    verify(questionnaireResponseSummaryRepository).save(
      questionnaireResponseSummary);

    verify(userQuestionnaireSummaryRepository).save(userQuestionnaireSummary);
  }

  public void updateUserSummaryForFirstTime() {

    when(questionResponseRepository.save(questionResponse1)).thenReturn(
      questionResponse1);

    when(
      questionResponseSummaryRepository.findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(
        user1, question1)).thenReturn(Optional.empty());

    when(
      questionnaireResponseSummaryRepository.findByAppraiseeAndQuestionnaireAndDeletedFalse(
        user1, questionnaire1)).thenReturn(Optional.empty());

    when(userQuestionnaireSummaryRepository.findFirstByAppraiseeAndDeletedFalse(
      user1)).thenReturn(Optional.empty());

    when(questionResponseSummaryRepository.save(
      questionResponseSummary1)).thenReturn(questionResponseSummary1);

    when(questionnaireResponseRepository.save(
      any(QuestionnaireResponse.class))).thenReturn(questionnaireResponse1);

    when(questionnaireResponseRepository
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire1, user1, memberLoggedIn
      )).thenReturn(Optional.ofNullable(questionnaireResponse1));


    when(questionnaireResponseSummaryRepository.save(
      questionnaireResponseSummary)).thenReturn(questionnaireResponseSummary);

    when(userQuestionnaireSummaryRepository.save(
      userQuestionnaireSummary)).thenReturn(userQuestionnaireSummary);


    myQuestionnaireService.updateUserSummary(
      questionnaire1, Arrays.asList(questionResponse1), memberLoggedIn,
      user1
    );

    verify(questionResponseRepository).save(questionResponse1);

    verify(
      questionResponseSummaryRepository).findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(
      user1, question1);

    verify(
      questionnaireResponseSummaryRepository).findByAppraiseeAndQuestionnaireAndDeletedFalse(
      user1, questionnaire1);

    verify(questionnaireResponseRepository)
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire1, user1, memberLoggedIn
      );

    verify(
      userQuestionnaireSummaryRepository).findFirstByAppraiseeAndDeletedFalse(
      user1);

    questionResponseSummary1.setId(null);
    questionResponseSummary1.setScoreSummary(answerUpdated);
    questionResponseSummary1.setQuestion(question1);
    questionResponseSummary1.setQuestionnaire(questionnaire1);
    questionResponseSummary1.setAppraisee(user1);

    verify(questionResponseSummaryRepository).save(questionResponseSummary1);

    verify(questionnaireResponseRepository).save(
      any(QuestionnaireResponse.class));

    questionnaireResponseSummary.setId(null);
    questionnaireResponseSummary.setQuestionnaire(questionnaire1);
    questionnaireResponseSummary.setAppraisee(user1);
    questionnaireResponseSummary.setScoreSummary(answerUpdated);

//    verify(questionnaireResponseSummaryRepository).save(
//      questionnaireResponseSummary);

    userQuestionnaireSummary.setId(null);
    userQuestionnaireSummary.setAppraisee(user1);
    userQuestionnaireSummary.setScoreSummary(answerUpdated);
    userQuestionnaireSummary.setCounter(1);

    verify(userQuestionnaireSummaryRepository).save(userQuestionnaireSummary);
  }

  @Test
  public void testUpdateScore() {
    when(questionResponseQueueRepository.findAll())
      .thenReturn(Arrays.asList(questionResponseQueue, questionResponseQueue2));
    doNothing().when(myQuestionnaireService).updateUserSummary(any(Questionnaire.class), anyListOf(QuestionResponse.class), any(User.class), any(User.class));

    myQuestionnaireService.updateScore();

    verify(myQuestionnaireService, times(2)).updateUserSummary(any(Questionnaire.class), anyListOf(QuestionResponse.class), any(User.class), any(User.class));
    verify(questionResponseQueueRepository).findAll();
    verify(questionResponseQueueRepository).delete(Arrays.asList(questionResponseQueue, questionResponseQueue2));
  }

  @Test
  public void testToQuestionResponse() {
    QuestionResponse q = myQuestionnaireService.toQuestionResponse(questionResponseQueue);

    assertThat(q.getAppraisee().getId()).isEqualTo(questionResponse1.getAppraisee().getId());
    assertThat(q.getAppraiser().getId()).isEqualTo(questionResponse1.getAppraiser().getId());
    assertThat(q.getQuestion().getQuestionnaire().getId()).isEqualTo(questionResponse1.getQuestion().getQuestionnaire().getId());
  }

  @Test
  public void testCreateQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser() {
//    doNothing().when(questionResponseQueueRepository)
//      .save(Arrays.asList(questionResponseQueue,questionResponseQueue2));
    when(questionnaireResponseRepository.findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
      questionnaire1,user1, memberLoggedIn
    )).thenReturn(Optional.empty());

    myQuestionnaireService.createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
      questionnaire1,
      Arrays.asList(questionResponseQueue,questionResponseQueue2),
      memberLoggedIn,
      user1
    );

    verify(questionnaireResponseRepository)
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(
        questionnaire1,user1, memberLoggedIn);
    verify(questionResponseQueueRepository)
      .save(Arrays.asList(questionResponseQueue,questionResponseQueue2));
    verify(questionnaireResponseRepository)
      .save(any(QuestionnaireResponse.class));
  }

  @Test
  public void testgetListAppraiseeDone(){

    when(questionnaireResponseRepository.findAllByQuestionnaireAndAppraiserAndDeletedFalse(
      questionnaire1,
      memberLoggedIn
    )).thenReturn(Arrays.asList(questionnaireResponse1));

    List<QuestionnaireResponse> ret = myQuestionnaireService.getListAppraiseeDone(
      questionnaire1,
      memberLoggedIn
    );

    verify(questionnaireResponseRepository)
      .findAllByQuestionnaireAndAppraiserAndDeletedFalse(
        questionnaire1, memberLoggedIn);

    assertThat(ret.size()).isEqualTo(1);
    assertThat(ret.get(0)).isEqualTo(questionnaireResponse1);
  }
}
