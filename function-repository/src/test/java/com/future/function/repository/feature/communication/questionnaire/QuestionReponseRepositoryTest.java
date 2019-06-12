package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class QuestionReponseRepositoryTest {

  private static final String ID_1 ="id_1";

  private static final String ID_2 ="id_2";

  private static final String QUESTION_ID = "question_id";

  private static final String ID_USER_1 = "id_user1";

  private Question question = Question.builder()
          .id(QUESTION_ID)
          .build();

  private User appraisee = User.builder()
          .id(ID_USER_1)
          .build();

  @Autowired
  private QuestionResponseRepository questionResponseRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Before
  public void SetUp() {
    QuestionResponse questionResponse1 = QuestionResponse.builder()
            .id(ID_1)
            .question(question)
            .appraisee(appraisee)
            .build();

    QuestionResponse questionResponse2 = QuestionResponse.builder()
            .id(ID_2)
            .question(question)
            .appraisee(appraisee)
            .build();

    userRepository.save(appraisee);
    questionRepository.save(question);
    questionResponseRepository.save(questionResponse1);
    questionResponseRepository.save(questionResponse2);

  }

  @After
  public void TearDown() {
    userRepository.deleteAll();
    questionResponseRepository.deleteAll();
  }

  @Test
  public void testGivenQuestionAndAppraiseeByFindingAllQuestionsReponseReturnedListQuestionsResponse() {
    List<QuestionResponse> questionResponses =
            questionResponseRepository.findAllByQuestionAndAppraisee(question, appraisee);

    System.out.println(questionResponses.get(0).toString());

    assertThat(questionResponses.size()).isEqualTo(2);

    assertThat(questionResponses.get(0).getId()).isEqualTo(ID_1);
    assertThat(questionResponses.get(0).getAppraisee().getId()).isEqualTo(ID_USER_1);
    assertThat(questionResponses.get(0).getQuestion().getId()).isEqualTo(QUESTION_ID);

    assertThat(questionResponses.get(1).getId()).isEqualTo(ID_2);
    assertThat(questionResponses.get(1).getAppraisee().getId()).isEqualTo(ID_USER_1);
    assertThat(questionResponses.get(1).getQuestion().getId()).isEqualTo(QUESTION_ID);
  }

}
