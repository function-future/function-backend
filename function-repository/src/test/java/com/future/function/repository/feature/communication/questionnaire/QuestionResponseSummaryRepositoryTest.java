package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class QuestionResponseSummaryRepositoryTest {

  private static final Pageable PAGEABLE = new PageRequest(0,10);

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String ID_USER_1 = "id_user1";

  private static final String QUESTION_ID1 = "question_id1";

  private static final String QUESTION_ID2 = "question_id2";

  private static final String QUESTIONNAIRE_ID = "questionnaire_id";

  private User appraisee = User.builder()
          .id(ID_USER_1)
          .build();

  private Question question1 = Question.builder()
          .id(QUESTION_ID1)
          .build();

  private Question question2 = Question.builder()
          .id(QUESTION_ID2)
          .build();

  private Questionnaire questionnaire = Questionnaire.builder()
          .id(QUESTIONNAIRE_ID)
          .build();

  @Autowired
  private QuestionResponseSummaryRepository questionResponseSummaryRepository;

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private UserRepository userRepository;

  @Before
  public void SetUp() {
    QuestionResponseSummary qRS1 = QuestionResponseSummary.builder()
            .id(ID_1)
            .appraisee(appraisee)
            .question(question1)
            .questionnaire(questionnaire)
            .build();

    QuestionResponseSummary qRS2 = QuestionResponseSummary.builder()
            .id(ID_2)
            .appraisee(appraisee)
            .question(question2)
            .questionnaire(questionnaire)
            .build();

    userRepository.save(appraisee);
    questionRepository.save(question1);
    questionRepository.save(question2);
    questionnaireRepository.save(questionnaire);
    questionResponseSummaryRepository.save(qRS1);
    questionResponseSummaryRepository.save(qRS2);
  }

  @After
  public void TearDown() {
    questionResponseSummaryRepository.deleteAll();
    questionnaireRepository.deleteAll();
    questionRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void
    testGivenQuestionnaireAndAppraiseeByFindingAllQuestionResponseSummaryReturnedListQuestionResponseSummary() {
      List<QuestionResponseSummary> questionResponseSummaries =
              questionResponseSummaryRepository
                      .findAllByQuestionnaireAndAppraisee(questionnaire, appraisee);

      assertThat(questionResponseSummaries.size()).isEqualTo(2);
      assertThat(questionResponseSummaries.get(0).getId()).isEqualTo(ID_1);
      assertThat(questionResponseSummaries.get(1).getId()).isEqualTo(ID_2);
  }

  @Test
  public void testGivenAppraiseeAndQuestionByFindingQuestionResponseSummaryReturnedQuestionResponseSummary() {
    Optional<QuestionResponseSummary> questionResponseSummary =
            questionResponseSummaryRepository.findAllByAppraiseeAndQuestion(appraisee,question1);

    assertThat(questionResponseSummary.get().getId()).isEqualTo(ID_1);
  }
}
