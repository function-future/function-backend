package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponse;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class QuestionnaireResponseRepositoryTest {

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String ID_USER_1 = "id_user1";

  private static final String ID_USER_2 = "id_user2";

  private static final String QUESTIONNAIRE_ID = "questionnaire_id";

  private static final String QUESTION_RESPONSE_ID1 = "question_response_id1";

  private static final String QUESTION_RESPONSE_ID2 = "question_response_id2";

  private User appraisee = User.builder()
    .id(ID_USER_1)
    .build();

  private User appraiser = User.builder()
    .id(ID_USER_2)
    .build();

  private Questionnaire questionnaire = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID)
    .build();

  private QuestionResponse questionResponse1 = QuestionResponse.builder()
    .id(QUESTION_RESPONSE_ID1)
    .build();

  private QuestionResponse questionResponse2 = QuestionResponse.builder()
    .id(QUESTION_RESPONSE_ID2)
    .build();

  private List<QuestionResponse> questionResponses = new ArrayList<>();

  @Autowired
  private QuestionnaireResponseRepository questionnaireResponseRepository;

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Autowired
  private QuestionResponseRepository questionResponseRepository;

  @Before
  public void SetUp() {

    questionResponseRepository.save(questionResponse1);
    questionResponseRepository.save(questionResponse2);
    questionnaireRepository.save(questionnaire);

    questionResponses.add(questionResponse1);
    questionResponses.add(questionResponse2);

    QuestionnaireResponse questionnaireResponse =
      QuestionnaireResponse.builder()
        .id(ID_1)
        .questionnaire(questionnaire)
        .appraisee(appraisee)
        .appraiser(appraiser)
        .details(questionResponses)
        .build();

    QuestionnaireResponse questionnaireResponse2 =
      QuestionnaireResponse.builder()
        .id(ID_2)
        .questionnaire(questionnaire)
        .appraisee(appraisee)
        .appraiser(appraiser)
        .details(questionResponses)
        .build();

    questionnaireResponse2.setDeleted(true);
    questionnaireResponseRepository.save(questionnaireResponse);
    questionnaireResponseRepository.save(questionnaireResponse2);
  }

  @After
  public void TearDown() {

    questionnaireResponseRepository.deleteAll();
    questionnaireRepository.deleteAll();
    questionResponseRepository.deleteAll();
  }

  @Test
  public void testGivenQuestionnaireAndAppraiseeByFindingAllQuestionnaireResponseReturnListQuestionnaireResponse() {

    List<QuestionnaireResponse> questionnaireResponses =
      questionnaireResponseRepository.findAllByQuestionnaireAndAppraiseeAndDeletedFalse(
        questionnaire, appraisee);

    assertThat(questionnaireResponses.size()).isEqualTo(1);
    assertThat(questionnaireResponses.get(0)
                 .getId()).isEqualTo(ID_1);
    assertThat(questionnaireResponses.get(0)
                 .getDetails()
                 .size()).isEqualTo(2);
    //    assertThat(questionnaireResponses.get(1).getId()).isEqualTo(ID_2);
    //    assertThat(questionnaireResponses.get(1).getDetails().size())
    //    .isEqualTo(2);
  }

  @Test
  public void testGivenQuestionnaireAndAppraiserByFindingAllQuestionnaireResponseReturnListQuestionnaireResponse(){

    List<QuestionnaireResponse> questionnaireResponses =
        questionnaireResponseRepository.findAllByQuestionnaireAndAppraiserAndDeletedFalse(questionnaire, appraiser);

    assertThat(questionnaireResponses.size()).isEqualTo(1);
    assertThat(questionnaireResponses.get(0)
      .getId()).isEqualTo(ID_1);
  }
}
