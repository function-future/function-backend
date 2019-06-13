package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionBankRepositoryTest {

  private static final String QUESTIONBANK_ID = "random-id";
  private static final String QUESTIONBANK_DESCRIPTION = "questionbank-description";

  @Autowired
  private QuestionBankRepository repository;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
    repository.deleteAll();
  }

  @Test
  public void testFindQuestionBankByIdAndDeletedFalse() {
    QuestionBank questionBank = QuestionBank
            .builder()
            .id(QUESTIONBANK_ID)
            .description(QUESTIONBANK_DESCRIPTION)
            .build();
    repository.save(questionBank);

    Optional<QuestionBank> actual = repository.findByIdAndDeletedFalse(QUESTIONBANK_ID);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(questionBank);
  }
}