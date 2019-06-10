package com.future.function.repository.feature.communication.questionnaire;


import com.future.function.model.entity.feature.communication.questionnaire.Question;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.repository.TestApplication;
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
public class QuestionRepositoryTest {

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String ID_3 = "id_3";

  private static final String TITLE_1 = "title_1";

  private static final String TITLE_2 = "title_2";

  private static final String DESCRIPTION = "Lorem Ipsum";

  private static final Questionnaire questionnaire1 =
          Questionnaire.builder()
            .id("id_questionnaire_1")
            .title(TITLE_1)
            .description(DESCRIPTION)
            .startDate(Long.valueOf(1559966400))
            .build();

  private static final Questionnaire questionnaire2 =
          Questionnaire.builder()
            .id("id_questionnaire_2")
            .title(TITLE_2)
            .description(DESCRIPTION)
            .startDate(Long.valueOf(1559966400))
            .build();

  @Autowired
  private QuestionRepository questionRepository;

  @Before
  public void SetUp() {
    Question question1 = Question.builder()
            .id(ID_1)
            .questionnaire(questionnaire1)
            .description(DESCRIPTION + "1")
            .build();

    Question question2 = Question.builder()
            .id(ID_2)
            .questionnaire(questionnaire1)
            .description(DESCRIPTION + "2")
            .build();

    Question question3 = Question.builder()
            .id(ID_3)
            .questionnaire(questionnaire2)
            .description(DESCRIPTION + "3")
            .build();


    questionRepository.save(question1);
    questionRepository.save(question2);
    questionRepository.save(question3);
  }

  @After
  public void TearDown() {
    questionRepository.deleteAll();
  }

  @Test
  public void testGivenQuestionnaireByFindingAllQuestionsReturnListQuestion() {
    List<Question> questions1 = questionRepository.findAllByQuestionnaire(questionnaire1);
    List<Question> questions2 = questionRepository.findAllByQuestionnaire(questionnaire2);

    assertThat(questions1.size()).isEqualTo(2);
    assertThat(questions1.get(0).getId()).isEqualTo(ID_1);
    assertThat(questions1.get(1).getId()).isEqualTo(ID_2);

    assertThat(questions2.size()).isEqualTo(1);
    assertThat(questions2.get(0).getId()).isEqualTo(ID_3);
  }

}
