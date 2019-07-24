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
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.UserQuestionnaireSummaryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Author: ricky.kennedy
 * Created At: 2:28 PM 7/24/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class MyQuestionnaireServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String USER_ID_1 = "userId1";

  private static final String USER_LOGGED_IN_ID_1 = "userLoggedInId1";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_RESPONSE_ID_1 = "questionnaireResponseId1";

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1 = "questionnaireResponseSummaryId1";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_1 = "questionnaireParticipantId1";

  private static final String QUESTION_ID_1 = "questionId1";

  private static final String QUESTION_RESPONSE_ID_1 = "questionResponseId1";

  private static final String QUESTION_RESPONSE_SUMMARY_ID_1 = "questionResponseSummaryId1";

  private static final float SCORE = (float) 5.0;

  private static final String USER_QUESTIONNAIRE_SUMMARY_ID_1 = "userQuestionnaireSummaryId1";

  private QuestionnaireParticipant questionnaireParticipant1;

  private User user1;

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

  @Mock
  private QuestionnaireParticipantRepository questionnaireParticipantRepository;

  @Mock
  private QuestionQuestionnaireRepository questionQuestionnaireRepository;

  @Mock
  private QuestionResponseRepository questionResponseRepository;

  @Mock
  private QuestionnaireResponseRepository questionnaireResponseRepository;

  @Mock
  private QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository;

  @Mock
  private UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  @Mock
  private QuestionResponseSummaryRepository questionResponseSummaryRepository;

  @InjectMocks
  private MyQuestionnaireServiceImpl myQuestionnaireService;

  @Before
  public void setUp() {
    user1 = User.builder()
      .id(USER_ID_1)
      .build();

    memberLoggedIn = User.builder()
      .id(USER_LOGGED_IN_ID_1)
      .build();

    questionnaire1 = Questionnaire.builder()
      .id(QUESTIONNAIRE_ID_1)
      .build();

    questionnaireParticipant1 = QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_1)
      .member(user1)
      .questionnaire(questionnaire1)
      .build();

    question1 = QuestionQuestionnaire.builder()
      .id(QUESTION_ID_1)
      .build();

    questionResponse1 = QuestionResponse.builder()
      .id(QUESTION_RESPONSE_ID_1)
      .question(question1)
      .score(SCORE)
      .appraisee(user1)
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
      .appraisee(user1)
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
    verifyNoMoreInteractions(
      questionnaireParticipantRepository,
      questionQuestionnaireRepository,
      questionResponseRepository,
      questionnaireResponseRepository,
      questionnaireResponseSummaryRepository,
      userQuestionnaireSummaryRepository,
      questionResponseSummaryRepository
    );
  }

  @Test
  public void getQuestionnairesByMemberLoginAsAppraiser() {
    when(questionnaireParticipantRepository
      .findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
        user1, ParticipantType.APPRAISER, PAGEABLE))
      .thenReturn(new PageImpl<>(Collections.singletonList(questionnaireParticipant1), PAGEABLE, 1));

    Page<Questionnaire> questionnairePage =
      myQuestionnaireService.getQuestionnairesByMemberLoginAsAppraiser(user1, PAGEABLE);

    assertThat(questionnairePage.getTotalElements()).isEqualTo(1);
    assertThat(questionnairePage.getContent().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID_1);

    verify(questionnaireParticipantRepository)
      .findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(
        user1, ParticipantType.APPRAISER, PAGEABLE);
  }

  @Test
  public void getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser() {
    when(questionnaireParticipantRepository
      .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
        questionnaire1, ParticipantType.APPRAISEE, PAGEABLE))
      .thenReturn(new PageImpl<>(Collections.singletonList(questionnaireParticipant1), PAGEABLE, 1));

    when(questionnaireResponseRepository
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(questionnaire1, user1, memberLoggedIn))
      .thenReturn(Optional.empty());

    List<QuestionnaireParticipant> questionnaireParticipants =
      myQuestionnaireService.getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(questionnaire1, memberLoggedIn);

    assertThat(questionnaireParticipants.size()).isEqualTo(1);

    verify(questionnaireParticipantRepository)
      .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(
        questionnaire1, ParticipantType.APPRAISEE, PAGEABLE);

    verify(questionnaireResponseRepository)
      .findByQuestionnaireAndAppraiseeAndAppraiserAndDeletedFalse(questionnaire1, user1, memberLoggedIn);
  }

  @Test
  public void getQuestionnaireParticipantById() {
    when(questionnaireParticipantRepository.findOne(QUESTIONNAIRE_PARTICIPANT_ID_1))
      .thenReturn(questionnaireParticipant1);

    QuestionnaireParticipant questionnaireParticipant=
      myQuestionnaireService.getQuestionnaireParticipantById(QUESTIONNAIRE_PARTICIPANT_ID_1);

    assertThat(questionnaireParticipant.getId()).isEqualTo(QUESTIONNAIRE_PARTICIPANT_ID_1);

    verify(questionnaireParticipantRepository).findOne(QUESTIONNAIRE_PARTICIPANT_ID_1);
  }

  @Test
  public void getQuestionsFromQuestionnaire() {

    when(questionQuestionnaireRepository.findAllByQuestionnaire(questionnaire1))
      .thenReturn(Arrays.asList(question1));

    List<QuestionQuestionnaire> questions =
      myQuestionnaireService.getQuestionsFromQuestionnaire(questionnaire1);

    assertThat(questions.size()).isEqualTo(1);
    assertThat(questions.get(0).getId()).isEqualTo(QUESTION_ID_1);

    verify(questionQuestionnaireRepository).findAllByQuestionnaire(questionnaire1);
  }

  @Test
  public void createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser() {

    when(questionResponseRepository.save(questionResponse1)).thenReturn(questionResponse1);

    when(questionResponseSummaryRepository
      .findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(user1, question1))
      .thenReturn(Optional.of(questionResponseSummary1));

    when(questionnaireResponseSummaryRepository
      .findByAppraiseeAndQuestionnaireAndDeletedFalse(user1, questionnaire1))
      .thenReturn(Optional.of(questionnaireResponseSummary));

    when(userQuestionnaireSummaryRepository
      .findFirstByAppraiseeAndDeletedFalse(user1)).thenReturn(Optional.of(userQuestionnaireSummary));

    when(questionResponseSummaryRepository.save(questionResponseSummary1))
      .thenReturn(questionResponseSummary1);

    when(questionnaireResponseRepository.save(any(QuestionnaireResponse.class)))
      .thenReturn(questionnaireResponse1);

    when(questionnaireResponseSummaryRepository.save(questionnaireResponseSummary))
      .thenReturn(questionnaireResponseSummary);

    when(userQuestionnaireSummaryRepository.save(userQuestionnaireSummary)).thenReturn(userQuestionnaireSummary);

    QuestionnaireResponse questionnaireResponse =
      myQuestionnaireService.createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
        questionnaire1,
        Arrays.asList(questionResponse1),
        memberLoggedIn,
        user1
      );

    assertThat(questionnaireResponse.getId()).isEqualTo(QUESTIONNAIRE_RESPONSE_ID_1);

    verify(questionResponseRepository).save(questionResponse1);

    verify(questionResponseSummaryRepository)
      .findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(user1, question1);

    verify(questionnaireResponseSummaryRepository)
      .findByAppraiseeAndQuestionnaireAndDeletedFalse(user1, questionnaire1);

    verify(userQuestionnaireSummaryRepository)
      .findFirstByAppraiseeAndDeletedFalse(user1);

    verify(questionResponseSummaryRepository).save(questionResponseSummary1);

    verify(questionnaireResponseRepository).save(any(QuestionnaireResponse.class));

    verify(questionnaireResponseSummaryRepository).save(questionnaireResponseSummary);

    verify(userQuestionnaireSummaryRepository).save(userQuestionnaireSummary);
  }

  @Test
  public void createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiserForFirstTime() {

    when(questionResponseRepository.save(questionResponse1)).thenReturn(questionResponse1);

    when(questionResponseSummaryRepository
      .findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(user1, question1))
      .thenReturn(Optional.empty());

    when(questionnaireResponseSummaryRepository
      .findByAppraiseeAndQuestionnaireAndDeletedFalse(user1, questionnaire1))
      .thenReturn(Optional.empty());

    when(userQuestionnaireSummaryRepository
      .findFirstByAppraiseeAndDeletedFalse(user1)).thenReturn(Optional.empty());

    when(questionResponseSummaryRepository.save(questionResponseSummary1))
      .thenReturn(questionResponseSummary1);

    when(questionnaireResponseRepository.save(any(QuestionnaireResponse.class)))
      .thenReturn(questionnaireResponse1);

    when(questionnaireResponseSummaryRepository.save(questionnaireResponseSummary))
      .thenReturn(questionnaireResponseSummary);

    when(userQuestionnaireSummaryRepository.save(userQuestionnaireSummary)).thenReturn(userQuestionnaireSummary);

    QuestionnaireResponse questionnaireResponse =
      myQuestionnaireService.createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
        questionnaire1,
        Arrays.asList(questionResponse1),
        memberLoggedIn,
        user1
      );

    assertThat(questionnaireResponse.getId()).isEqualTo(QUESTIONNAIRE_RESPONSE_ID_1);

    verify(questionResponseRepository).save(questionResponse1);

    verify(questionResponseSummaryRepository)
      .findByAppraiseeAndQuestionQuestionnaireAndDeletedFalse(user1, question1);

    verify(questionnaireResponseSummaryRepository)
      .findByAppraiseeAndQuestionnaireAndDeletedFalse(user1, questionnaire1);

    verify(userQuestionnaireSummaryRepository)
      .findFirstByAppraiseeAndDeletedFalse(user1);

    questionResponseSummary1.setId(null);
    questionResponseSummary1.setScoreSummary(answerUpdated);
    questionResponseSummary1.setQuestion(question1);
    questionResponseSummary1.setQuestionnaire(questionnaire1);
    questionResponseSummary1.setAppraisee(user1);

    verify(questionResponseSummaryRepository).save(questionResponseSummary1);

    verify(questionnaireResponseRepository).save(any(QuestionnaireResponse.class));

    questionnaireResponseSummary.setId(null);
    questionnaireResponseSummary.setQuestionnaire(questionnaire1);
    questionnaireResponseSummary.setAppraisee(user1);
    questionnaireResponseSummary.setScoreSummary(answerUpdated);

    verify(questionnaireResponseSummaryRepository).save(questionnaireResponseSummary);

    userQuestionnaireSummary.setId(null);
    userQuestionnaireSummary.setAppraisee(user1);
    userQuestionnaireSummary.setScoreSummary(answerUpdated);
    userQuestionnaireSummary.setCounter(1);

    verify(userQuestionnaireSummaryRepository).save(userQuestionnaireSummary);
  }
}
