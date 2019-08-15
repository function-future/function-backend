package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
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
public class QuestionnaireResponseSummaryRepositoryTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String ID_USER_1 = "id_user1";

  private static final String QUESTIONNAIRE_ID = "questionnaire_id";

  private static final String QUESTIONNAIRE_ID2 = "questionnaire_id2";

  private User appraisee = User.builder()
    .id(ID_USER_1)
    .build();

  private Questionnaire questionnaire = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID)
    .build();

  private Questionnaire questionnaire2 = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID2)
    .build();

  @Autowired
  private QuestionnaireResponseSummaryRepository
    questionnaireResponseSummaryRepository;

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Autowired
  private UserRepository userRepository;

  @Before
  public void SetUp() {

    userRepository.save(appraisee);
    questionnaireRepository.save(questionnaire);
    questionnaireRepository.save(questionnaire2);

    QuestionnaireResponseSummary questionnaireResponseSummary =
      QuestionnaireResponseSummary.builder()
        .id(ID_1)
        .questionnaire(questionnaire)
        .appraisee(appraisee)
        .build();

    QuestionnaireResponseSummary questionnaireResponseSummary2 =
      QuestionnaireResponseSummary.builder()
        .id(ID_2)
        .questionnaire(questionnaire2)
        .appraisee(appraisee)
        .build();

    questionnaireResponseSummary2.setDeleted(true);
    questionnaireResponseSummaryRepository.save(questionnaireResponseSummary);
    questionnaireResponseSummaryRepository.save(questionnaireResponseSummary2);
  }

  @After
  public void TearDown() {

    userRepository.deleteAll();
    questionnaireRepository.deleteAll();
    questionnaireResponseSummaryRepository.deleteAll();
  }

  @Test
  public void testGivenAppraiseeByFindingAllQuesitonnaireResponseSummaryReturnPagedQuestionnaireResponseSummary() {

    Page<QuestionnaireResponseSummary> questionnaireResponseSummaries =
      questionnaireResponseSummaryRepository.findAllByAppraiseeAndDeletedFalse(
        appraisee, PAGEABLE);

    assertThat(questionnaireResponseSummaries.getTotalElements()).isEqualTo(1);
    assertThat(questionnaireResponseSummaries.getContent()
                 .get(0)
                 .getId()).isEqualTo(ID_1);
    assertThat(questionnaireResponseSummaries.getContent()
                 .get(0)
                 .getQuestionnaire()
                 .getId()).isEqualTo(QUESTIONNAIRE_ID);
    //    assertThat(questionnaireResponseSummaries.getContent().get(1).getId
    //    ()).isEqualTo(ID_2);
    //    assertThat(questionnaireResponseSummaries.getContent().get(1)
    //    .getQuestionnaire().getId()).isEqualTo(QUESTIONNAIRE_ID2);

  }

}
