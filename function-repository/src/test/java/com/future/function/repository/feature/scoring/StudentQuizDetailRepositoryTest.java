package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentQuizDetailRepositoryTest {

  private static final String STUDENT_QUIZ_ID = "student-quiz-id";

  private static final String STUDENT_QUIZ_DETAIL_ID = "student-quiz-detail-id";
  private static final int POINT = 100;

  private StudentQuiz studentQuiz;
  private StudentQuizDetail studentQuizDetail;

  @Autowired
  private StudentQuizDetailRepository repository;

  @Before
  public void setUp() throws Exception {

    studentQuiz = StudentQuiz
        .builder()
        .id(STUDENT_QUIZ_ID)
        .build();

    studentQuizDetail = StudentQuizDetail
        .builder()
        .id(STUDENT_QUIZ_DETAIL_ID)
        .studentQuiz(studentQuiz)
        .point(POINT)
        .build();

    repository.save(studentQuizDetail);

  }

  @After
  public void tearDown() throws Exception {
    repository.deleteAll();
  }

  @Test
  public void findByIdAndDeletedFalse() {
    Optional<StudentQuizDetail> actual = repository.findByIdAndDeletedFalse(STUDENT_QUIZ_DETAIL_ID);

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isNotNull();
  }

  @Test
  public void findFirstByStudentId() {
    Optional<StudentQuizDetail> actual = repository.findTopByStudentQuizIdAndDeletedFalseOrderByCreatedAtDesc(STUDENT_QUIZ_ID);

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isNotNull();
  }
}
