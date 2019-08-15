package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptionRepositoryTest {

  private static final String OPTION_LABEL = "option";

  private static final String QUESTION_ID = "id";

  private Question question;

  private Option option;

  @Autowired
  private OptionRepository optionRepository;

  @Before
  public void setUp() throws Exception {

    option = Option.builder()
      .label(OPTION_LABEL)
      .correct(true)
      .build();
    question = Question.builder()
      .id(QUESTION_ID)
      .build();
    option.setQuestion(question);
    optionRepository.save(option);
  }

  @After
  public void tearDown() throws Exception {

    optionRepository.deleteAll();
  }

  @Test
  public void findByIdAndDeletedFalse() {

    String id = option.getId();

    Optional<Option> actual = optionRepository.findByIdAndDeletedFalse(id);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isNotNull();
  }

  @Test
  public void findAllByQuestionId() {

    List<Option> optionList = optionRepository.findAllByQuestionId(QUESTION_ID);
    assertThat(optionList.size()).isEqualTo(1);
    assertThat(optionList.get(0)
                 .getLabel()).isEqualTo(option.getLabel());
  }

}
