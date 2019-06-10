package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class QuestionnaireParticipantRepositoryTest {

  public static final String ID_1 ="id_1";

  public static final String ID_2 ="id_2";

  public static final String ID_QUESTIONNAIRE1 = "id_questionnaire1";

  public static final String ID_QUESTIONNAIRE2 = "id_questionnaire2";

  private static final Questionnaire questionnaire1 = Questionnaire.builder()
          .id(ID_QUESTIONNAIRE1)
          .build();

  private static final Questionnaire questionnaire2 = Questionnaire.builder()
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
            .build();
    QuestionnaireParticipant questionnaireParticipant2 = QuestionnaireParticipant.builder()
            .id(ID_1)
            .build();

    userRepository.save(user1);
    questionnaireRepository.save(questionnaire1);
    questionnaireRepository.save(questionnaire2);
  }

  @Test
  public void tes() {
    assertTrue(true);
  }

}
