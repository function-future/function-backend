package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuizRepositoryTest {

  private String QUIZ_ID = UUID.randomUUID().toString();
  private String QUIZ_TITLE = "quiz-title";
  private String QUIZ_DESCRIPTION = "quiz-description";
  private long DATE = 0;
  private long TIME_LIMIT = 0;
  private int TRIALS = 0;
  private int QUESTION_COUNT = 0;

  private int PAGE = 0;
  private int TOTAL = 10;

  private Quiz quiz;
  private Pageable pageable;
  private List<Quiz> quizList;

  @Autowired
  private QuizRepository quizRepository;

  @Before
  public void setUp() throws Exception {
    quiz = Quiz
            .builder()
            .id(QUIZ_ID)
            .title(QUIZ_TITLE)
            .description(QUIZ_DESCRIPTION)
            .startDate(DATE)
            .endDate(DATE)
            .timeLimit(TIME_LIMIT)
            .trials(TRIALS)
            .questionCount(QUESTION_COUNT)
            .build();

    quizList = Collections.singletonList(quiz);

    pageable = new PageRequest(PAGE, TOTAL);

    quizRepository.save(quiz);
  }

  @After
  public void tearDown() throws Exception {
    quizRepository.deleteAll();
  }

  @Test
  public void testFindQuizByIdAndDeletedFalse() {
    Optional<Quiz> actual = quizRepository.findByIdAndDeletedFalse(QUIZ_ID);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(quiz);
  }

  @Test
  public void testDeleteQuizById() {
    quizRepository.delete(QUIZ_ID);
    Optional<Quiz> actual = quizRepository.findByIdAndDeletedFalse(QUIZ_ID);
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  public void testSaveQuizWithQuizObject() {
    quizRepository.save(quiz);
    Optional<Quiz> actual = quizRepository.findByIdAndDeletedFalse(QUIZ_ID);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(quiz);
  }

  @Test
  public void testFindAllQuizPageWithPageableFilterAndSearch() {
    Page<Quiz> actual = quizRepository.findAll(pageable);
    assertThat(actual.getContent()).isEqualTo(quizList);
  }
}