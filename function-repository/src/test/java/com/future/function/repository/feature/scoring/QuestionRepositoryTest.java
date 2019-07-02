package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionRepositoryTest {

  private static final String QUESTION_TEXT = "question";

  private static final String QUESTION_BANK_ID = "question-bank-id";

  private Question question;

  private QuestionBank questionBank;

  private Pageable pageable;

  private Page<Question> questionPage;

  @Autowired
  private QuestionRepository repository;

  @Before
  public void setUp() throws Exception {
    questionBank = QuestionBank.builder().id(QUESTION_BANK_ID).build();

    question = Question.builder()
        .label(QUESTION_TEXT)
        .questionBank(questionBank)
        .build();
    question.setQuestionBank(questionBank);
    repository.save(question);

    pageable = new PageRequest(0, 10);

    questionPage = new PageImpl<>(Collections.singletonList(question), pageable, 1);
  }

  @After
  public void tearDown() throws Exception {
    repository.deleteAll();
  }

  @Test
  public void findByIdTest() {
    String id = question.getId();

    Optional<Question> actual = repository.findByIdAndDeletedFalse(id);
    assertThat(actual).isNotNull();
    assertThat(actual.isPresent()).isTrue();
  }

  @Test
  public void findAllByQuestionBankIdPageableTest() {
    Page<Question> actual = repository.findAllByQuestionBankIdAndDeletedFalse(QUESTION_BANK_ID, pageable);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    question.setQuestionBank(null);
    assertThat(actual.getContent().get(0).getLabel()).isEqualTo(question.getLabel());
  }

  @Test
  public void findAllByQuestionBankIdListTest() {
    List<Question> actual = repository.findAllByQuestionBankIdAndDeletedFalse(QUESTION_BANK_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getLabel()).isEqualTo(question.getLabel());
  }
}
