package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.QuestionQuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireParticipantRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireRepository;
import com.future.function.service.api.feature.core.UserService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_ID_1_UPDATED = "questionnaireId1Updated";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_1 = "questionnaireParticipantId1";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_2 = "questionnaireParticipantId2";

  private static final String KEYWORD = "keyword";

  private static final String AUTHOR_ID = "authorID";

  private static final String QUESTION_ID_1 = "questionId1";

  private static final String QUESTION_ID_2 = "questionId2";

  private static final String QUESTION_ID_1_UPDATED = "questionId1Updated";

  private static final String PARTICIPANT_ID_1 = "participantId1";

  private static final String USER_ID_1 = "userId1";

  private User author;

  private Questionnaire questionnaire1;

  private Questionnaire questionnaire1Updated;

  private QuestionnaireParticipant questionnaireParticipant1;

  private QuestionnaireParticipant questionnaireParticipant2;

  private List<QuestionnaireParticipant> questionnaireParticipants = new ArrayList<>();

  private QuestionQuestionnaire question1;

  private QuestionQuestionnaire question2;

  private List<QuestionQuestionnaire> questions = new ArrayList<>();

  private QuestionQuestionnaire question1Updated;

  private QuestionnaireParticipant participant1;

  private User user1;

  @Mock
  private QuestionnaireRepository questionnaireRepository;

  @Mock
  private QuestionQuestionnaireRepository questionQuestionnaireRepository;

  @Mock
  private UserService userService;

  @Mock
  private QuestionnaireParticipantRepository questionnaireParticipantRepository;

  @InjectMocks
  private QuestionnaireServiceImpl questionnaireService;

  @Before
  public void setUp() {
    author = User.builder().id(AUTHOR_ID).build();

    questionnaire1 = Questionnaire.builder()
      .id(QUESTIONNAIRE_ID_1)
      .author(author)
      .build();

    questionnaire1Updated = Questionnaire.builder()
      .id(QUESTIONNAIRE_ID_1_UPDATED)
      .author(author)
      .build();

    questionnaireParticipant1 = QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_1)
      .build();

    questionnaireParticipant2 = QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_2)
      .build();

    questionnaireParticipants.add(questionnaireParticipant1);
    questionnaireParticipants.add(questionnaireParticipant2);

    question1 = QuestionQuestionnaire.builder().id(QUESTION_ID_1).build();

    question2 = QuestionQuestionnaire.builder().id(QUESTION_ID_2).build();

    questions.add(question1);
    questions.add(question2);

    question1.setQuestionnaire(questionnaire1);
    question1Updated = QuestionQuestionnaire.builder().id(QUESTION_ID_1_UPDATED).build();

    participant1 = QuestionnaireParticipant
      .builder()
      .id(PARTICIPANT_ID_1)
      .build();

    user1 = User.builder().id(USER_ID_1).build();
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(questionnaireRepository, questionQuestionnaireRepository,
                              userService, questionnaireParticipantRepository);
  }

  @Test
  public void getAllQuestionnaires() {
    when(questionnaireRepository.findAllByDeletedFalseOrderByCreatedAtDesc(PAGEABLE))
      .thenReturn(new PageImpl<>(Collections.singletonList(questionnaire1), PAGEABLE, 1));

    Page<Questionnaire> questionnairePage = questionnaireService.getAllQuestionnaires(PAGEABLE);
    assertThat(questionnairePage.getTotalElements()).isEqualTo(1);
    assertThat(questionnairePage.getContent().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID_1);

    verify(questionnaireRepository).findAllByDeletedFalseOrderByCreatedAtDesc(PAGEABLE);
  }

  @Test
  public void getQuestionnairesWithKeyword() {
    when(questionnaireRepository.findAllByTitleIgnoreCaseContainingAndDeletedFalseOrderByCreatedAtDesc(KEYWORD,PAGEABLE))
      .thenReturn(new PageImpl<>(Collections.singletonList(questionnaire1), PAGEABLE, 1));

    Page<Questionnaire> questionnairePage = questionnaireService.getQuestionnairesWithKeyword(KEYWORD, PAGEABLE);
    assertThat(questionnairePage.getTotalElements()).isEqualTo(1);
    assertThat(questionnairePage.getContent().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID_1);

    verify(questionnaireRepository).findAllByTitleIgnoreCaseContainingAndDeletedFalseOrderByCreatedAtDesc(KEYWORD,PAGEABLE);
  }

  @Test
  public void getQuestionnaire() {
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1))
      .thenReturn(questionnaire1);

    Questionnaire questionnaireResult = questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID_1);

    assertThat(questionnaireResult.getId()).isEqualTo(questionnaire1.getId());

    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
  }

  @Test
  public void createQuestionnaire() {
    when(questionnaireRepository.save(questionnaire1)).thenReturn(questionnaire1);
    when(userService.getUser(AUTHOR_ID)).thenReturn(author);

    Questionnaire questionnaireResult = questionnaireService.createQuestionnaire(questionnaire1, author);

    assertThat(questionnaireResult.getId()).isEqualTo(questionnaire1.getId());
    assertThat(questionnaireResult.getAuthor().getId()).isEqualTo(AUTHOR_ID);

    verify(questionnaireRepository).save(questionnaire1);
    verify(userService).getUser(AUTHOR_ID);
  }

  @Test
  public void updateQuestionnaire() {
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);
    when(questionnaireRepository.save(questionnaire1)).thenReturn(questionnaire1Updated);

    Questionnaire questionnaireResult = questionnaireService.updateQuestionnaire(questionnaire1);

    assertThat(questionnaireResult.getId()).isEqualTo(QUESTIONNAIRE_ID_1_UPDATED);

    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
    verify(questionnaireRepository).save(questionnaire1);
  }

  @Test
  public void deleteQuestionnaire() {
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);
    when(questionnaireRepository.save(questionnaire1)).thenReturn(questionnaire1);
    when(questionnaireParticipantRepository.findAllByQuestionnaireAndDeletedFalse(questionnaire1)).thenReturn(questionnaireParticipants);
    when(questionnaireParticipantRepository.save(questionnaireParticipant1)).thenReturn(questionnaireParticipant1);
    when(questionnaireParticipantRepository.save(questionnaireParticipant2)).thenReturn(questionnaireParticipant2);

    questionnaireService.deleteQuestionnaire(QUESTIONNAIRE_ID_1);

    assertThat(questionnaire1.isDeleted()).isTrue();
    assertThat(questionnaireParticipant1.isDeleted()).isTrue();
    assertThat(questionnaireParticipant2.isDeleted()).isTrue();

    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
    verify(questionnaireRepository).save(questionnaire1);
    verify(questionnaireParticipantRepository).findAllByQuestionnaireAndDeletedFalse(questionnaire1);
    verify(questionnaireParticipantRepository, times(1)).save(questionnaireParticipant1);
    verify(questionnaireParticipantRepository, times(1)).save(questionnaireParticipant2);
  }

  @Test
  public void getQuestionsByIdQuestionnaire() {
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);
    when(questionQuestionnaireRepository.findAllByQuestionnaire(questionnaire1)).thenReturn(questions);

    List<QuestionQuestionnaire> questionResult = questionnaireService.getQuestionsByIdQuestionnaire(QUESTIONNAIRE_ID_1);

    assertThat(questionResult.size()).isEqualTo(2);
    assertThat(questionResult.get(0).getId()).isEqualTo(QUESTION_ID_1);
    assertThat(questionResult.get(1).getId()).isEqualTo(QUESTION_ID_2);

    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
    verify(questionQuestionnaireRepository).findAllByQuestionnaire(questionnaire1);
  }

  @Test
  public void getQuestionQuestionnaire() {
    when(questionQuestionnaireRepository.findOne(QUESTION_ID_1)).thenReturn(question1);

    QuestionQuestionnaire questionResult = questionnaireService.getQuestionQuestionnaire(QUESTION_ID_1);

    assertThat(questionResult.getId()).isEqualTo(QUESTION_ID_1);

    verify(questionQuestionnaireRepository).findOne(QUESTION_ID_1);
  }

  @Test
  public void createQuestionQuestionnaire() {
  when(questionQuestionnaireRepository.save(question1)).thenReturn(question1);
  when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);

  QuestionQuestionnaire questionResult = questionnaireService.createQuestionQuestionnaire(question1);

  assertThat(questionResult.getId()).isEqualTo(QUESTION_ID_1);

  verify(questionQuestionnaireRepository).save(question1);
  verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);

  }

  @Test
  public void updateQuestionQuestionnaire() {
    when(questionQuestionnaireRepository.findOne(QUESTION_ID_1)).thenReturn(question1);
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);
    when(questionQuestionnaireRepository.save(question1)).thenReturn(question1Updated);

    QuestionQuestionnaire questionResult = questionnaireService.updateQuestionQuestionnaire(question1);

    assertThat(questionResult.getId()).isEqualTo(question1Updated.getId());

    verify(questionQuestionnaireRepository).findOne(QUESTION_ID_1);
    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
    verify(questionQuestionnaireRepository).save(question1);
  }

  @Test
  public void deleteQuestionQuestionnaire() {
    when(questionQuestionnaireRepository.findOne(QUESTION_ID_1)).thenReturn(question1);
    when(questionQuestionnaireRepository.save(question1)).thenReturn(question1);

    questionnaireService.deleteQuestionQuestionnaire(QUESTION_ID_1);

    assertThat(question1.isDeleted()).isTrue();

    verify(questionQuestionnaireRepository).findOne(QUESTION_ID_1);
    verify(questionQuestionnaireRepository).save(question1);

  }

  @Test
  public void getQuestionnaireAppraiser() {
    when(questionnaireParticipantRepository
      .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire1, ParticipantType.APPRAISER, PAGEABLE))
      .thenReturn(new PageImpl<>(Collections.singletonList(participant1), PAGEABLE, 1));

    Page<QuestionnaireParticipant> participants =
      questionnaireService.getQuestionnaireAppraiser(questionnaire1, PAGEABLE);

    assertThat(participants.getTotalElements()).isEqualTo(1);
    assertThat(participants.getContent().get(0).getId()).isEqualTo(PARTICIPANT_ID_1);

    verify(questionnaireParticipantRepository)
      .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire1, ParticipantType.APPRAISER, PAGEABLE);
  }

  @Test
  public void addQuestionnaireAppraiserToQuestionnaire() {
    participant1.setParticipantType(ParticipantType.APPRAISER);
    when(userService.getUser(USER_ID_1)).thenReturn(user1);
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);
    when(questionnaireParticipantRepository.save(any(QuestionnaireParticipant.class))).thenReturn(participant1);

    QuestionnaireParticipant participantResult =
      questionnaireService.addQuestionnaireAppraiserToQuestionnaire(QUESTIONNAIRE_ID_1, USER_ID_1);

    assertThat(participantResult.getId()).isEqualTo(PARTICIPANT_ID_1);
    assertThat(participantResult.getParticipantType()).isEqualTo(ParticipantType.APPRAISER);

    verify(userService).getUser(USER_ID_1);
    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
    verify(questionnaireParticipantRepository).save(any(QuestionnaireParticipant.class));
  }

  @Test
  public void deleteQuestionnaireAppraiserFromQuestionnaire() {
    participant1.setParticipantType(ParticipantType.APPRAISER);
    when(questionnaireParticipantRepository.findOne(PARTICIPANT_ID_1)).thenReturn(participant1);
    when(questionnaireParticipantRepository.save(participant1)).thenReturn(participant1);

    questionnaireService.deleteQuestionnaireAppraiserFromQuestionnaire(PARTICIPANT_ID_1);

    assertThat(participant1.getParticipantType()).isEqualTo(ParticipantType.UNKNOWN);
    assertThat(participant1.isDeleted()).isTrue();

    verify(questionnaireParticipantRepository).findOne(PARTICIPANT_ID_1);
    verify(questionnaireParticipantRepository).save(participant1);
  }

  @Test
  public void getQuestionnaireAppraisee() {
    when(questionnaireParticipantRepository
      .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire1, ParticipantType.APPRAISEE, PAGEABLE))
      .thenReturn(new PageImpl<>(Collections.singletonList(participant1), PAGEABLE, 1));

    Page<QuestionnaireParticipant> participants =
      questionnaireService.getQuestionnaireAppraisee(questionnaire1, PAGEABLE);

    assertThat(participants.getTotalElements()).isEqualTo(1);
    assertThat(participants.getContent().get(0).getId()).isEqualTo(PARTICIPANT_ID_1);

    verify(questionnaireParticipantRepository)
      .findAllByQuestionnaireAndParticipantTypeAndDeletedFalse(questionnaire1, ParticipantType.APPRAISEE, PAGEABLE);
  }

  @Test
  public void addQuestionnaireAppraiseeToQuestionnaire() {
    participant1.setParticipantType(ParticipantType.APPRAISEE);
    when(userService.getUser(USER_ID_1)).thenReturn(user1);
    when(questionnaireRepository.findOne(QUESTIONNAIRE_ID_1)).thenReturn(questionnaire1);
    when(questionnaireParticipantRepository.save(any(QuestionnaireParticipant.class))).thenReturn(participant1);

    QuestionnaireParticipant participantResult =
      questionnaireService.addQuestionnaireAppraiseeToQuestionnaire(QUESTIONNAIRE_ID_1, USER_ID_1);

    assertThat(participantResult.getId()).isEqualTo(PARTICIPANT_ID_1);

    verify(userService).getUser(USER_ID_1);
    verify(questionnaireRepository).findOne(QUESTIONNAIRE_ID_1);
    verify(questionnaireParticipantRepository).save(any(QuestionnaireParticipant.class));
  }

  @Test
  public void deleteQuestionnaireAppraiseeFromQuestionnaire() {
    participant1.setParticipantType(ParticipantType.APPRAISEE);
    when(questionnaireParticipantRepository.findOne(PARTICIPANT_ID_1)).thenReturn(participant1);
    when(questionnaireParticipantRepository.save(participant1)).thenReturn(participant1);

    questionnaireService.deleteQuestionnaireAppraiseeFromQuestionnaire(PARTICIPANT_ID_1);

    assertThat(participant1.getParticipantType()).isEqualTo(ParticipantType.UNKNOWN);
    assertThat(participant1.isDeleted()).isTrue();

    verify(questionnaireParticipantRepository).findOne(PARTICIPANT_ID_1);
    verify(questionnaireParticipantRepository).save(participant1);
  }
}
