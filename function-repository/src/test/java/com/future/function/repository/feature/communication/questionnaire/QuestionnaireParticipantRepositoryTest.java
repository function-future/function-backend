package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class QuestionnaireParticipantRepositoryTest {

  public static final String ID_1 ="id_1";

  public static final String ID_2 ="id_2";

  public static final String ID_QUESTIONNAIRE1 = "id_questionnaire1";

  public static final String ID_QUESTIONNAIRE2 = "id_questionnaire2";

  private static final Pageable PAGEABLE = new PageRequest(0,10);

  private final Questionnaire questionnaire1 = Questionnaire.builder()
          .id(ID_QUESTIONNAIRE1)
          .build();

  private final Questionnaire questionnaire2 = Questionnaire.builder()
          .id(ID_QUESTIONNAIRE2)
          .build();

  private static final String ID_USER_1 = "id_user1";

  private User user1 = User.builder()
          .id(ID_USER_1)
          .build();


  @Autowired
  private QuestionnaireParticipantRepository questionnaireParticipantRepository;

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Autowired
  private UserRepository userRepository;

  @Before
  public void SetUp() {

    QuestionnaireParticipant questionnaireParticipant = QuestionnaireParticipant.builder()
            .id(ID_1)
            .questionnaire(questionnaire1)
            .member(user1)
            .participantType(ParticipantType.fromString("APPRAISER"))
            .build();

    QuestionnaireParticipant questionnaireParticipant2 = QuestionnaireParticipant.builder()
            .id(ID_2)
            .questionnaire(questionnaire2)
            .member(user1)
            .participantType(ParticipantType.fromString("APPRAISER"))
            .build();

    questionnaireParticipant2.setDeleted(true);
    userRepository.save(user1);
    questionnaireRepository.save(questionnaire1);
    questionnaireRepository.save(questionnaire2);
    questionnaireParticipantRepository.save(questionnaireParticipant);
    questionnaireParticipantRepository.save(questionnaireParticipant2);
  }

  @After
  public void TearDown() {
    userRepository.deleteAll();
    questionnaireRepository.deleteAll();
    questionnaireParticipantRepository.deleteAll();
  }

  @Test
  public void
    testByGivenMemberAndParticipantTypeByFindingAllQuestionnaireParticipantReturnPagedQuestionnaireParticipant() {
      Page<QuestionnaireParticipant> questionnaireParticipants =
              questionnaireParticipantRepository
                      .findAllByMemberAndParticipantTypeAndDeletedFalseOrderByCreatedAtDesc(user1, ParticipantType.fromString("APPRAISER"), PAGEABLE);

      assertThat(questionnaireParticipants.getTotalElements()).isEqualTo(1);
      assertThat(questionnaireParticipants.getContent().get(0).getId()).isEqualTo(ID_1);
      assertThat(questionnaireParticipants.getContent().get(0).getMember().getId()).isEqualTo(ID_USER_1);
      assertThat(questionnaireParticipants.getContent().get(0).getQuestionnaire().getId()).isEqualTo(ID_QUESTIONNAIRE1);
//      assertThat(questionnaireParticipants.getContent().get(1).getId()).isEqualTo(ID_2);
//      assertThat(questionnaireParticipants.getContent().get(1).getMember().getId()).isEqualTo(ID_USER_1);
//      assertThat(questionnaireParticipants.getContent().get(1).getQuestionnaire().getId()).isEqualTo(ID_QUESTIONNAIRE2);
  }

  @Test
  public void testByGivenQuestionnaireByFindingAllQuestionnaireParticipantReturnListQuestionnaireParticipant() {
    Page<QuestionnaireParticipant> questionnaireParticipants =
            questionnaireParticipantRepository.findAllByQuestionnaireAndDeletedFalse(questionnaire1, PAGEABLE);

//    System.out.println(questionnaireParticipants.get(0)./toString());
    assertThat(questionnaireParticipants.getTotalElements()).isEqualTo(1);
    assertThat(questionnaireParticipants.getContent().get(0).getId()).isEqualTo(ID_1);
    assertThat(questionnaireParticipants.getContent().get(0).getMember().getId()).isEqualTo(ID_USER_1);
    assertThat(questionnaireParticipants.getContent().get(0).getQuestionnaire().getId()).isEqualTo(ID_QUESTIONNAIRE1);

    questionnaireParticipants =
      questionnaireParticipantRepository.findAllByQuestionnaireAndDeletedFalse(questionnaire2, PAGEABLE);

    assertThat(questionnaireParticipants.getTotalElements()).isEqualTo(0);

  }
}
