package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class QuestionnaireRepositoryTest {

  private static final Pageable PAGEABLE = new PageRequest(0,10);

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String TITLE_1 = "abc";

  private static final String TITLE_2 = "bcd";

  private static final String DESCRIPTION = "Lorem Ipsum";

  @Autowired
  private QuestionnaireRepository questionnaireRepository;

  @Before
  public void SetUp() {
    Questionnaire questionnaire1 = Questionnaire.builder()
            .id(ID_1)
            .title(TITLE_1)
            .description(DESCRIPTION)
            .startDate(Long.valueOf(1559966400))
            .build();

    Questionnaire questionnaire2 = Questionnaire.builder()
            .id(ID_2)
            .title(TITLE_2)
            .description(DESCRIPTION)
            .startDate(Long.valueOf(1559966400))
            .build();

    questionnaireRepository.save(questionnaire1);
    questionnaireRepository.save(questionnaire2);
  }

  @After
  public void tearDown() {
    questionnaireRepository.deleteAll();
  }

  @Test
  public void testByFindingAllQuestionnaireReturnPagedQuestionnaire() {
    Page<Questionnaire> questionnaires = questionnaireRepository.findAll(PAGEABLE);

    assertThat(questionnaires.getTotalElements()).isEqualTo(2);
    assertThat(questionnaires.getContent().get(0).getTitle()).isEqualTo(TITLE_1);
    assertThat(questionnaires.getContent().get(1).getTitle()).isEqualTo(TITLE_2);
  }

  @Test
  public void testGivenQuestionnaireIdByFindindQuestionnaireReturnQuestionnaire() {
    Optional<Questionnaire> questionnaire1 = questionnaireRepository.findById(ID_1);
    Optional<Questionnaire> questionnaire2 = questionnaireRepository.findById(ID_2);

    assertThat(questionnaire1.get().getId()).isEqualTo(ID_1);
    assertThat(questionnaire2.get().getId()).isEqualTo(ID_2);

  }

  @Test
  public void testGivenTitleSoughtByFindingAllQuestionnaireReturnPagedQuestionnaire() {
    Page<Questionnaire> questionnaires = questionnaireRepository.findAllByTitleIgnoreCaseContaining("bc", PAGEABLE);

    assertThat(questionnaires.getTotalElements()).isEqualTo(2);
    assertThat(questionnaires.getContent().get(0).getTitle()).isEqualTo(TITLE_1);
    assertThat(questionnaires.getContent().get(1).getTitle()).isEqualTo(TITLE_2);
  }
}
