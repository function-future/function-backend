package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import java.util.Optional;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentQuizRepositoryTest {

  private static final String QUIZ_ID = "quiz-id";
  private static final String QUIZ_TEXT = "quiz-text";

  private static final String USER_ID = "student-id";
  private static final String USER_NAME = "user-name";

  private static final String STUDENT_QUIZ_ID = "student-quiz-id";
  private static final boolean DONE = true;
  private static final int TRIALS = 3;

  private StudentQuiz studentQuiz;
  private Quiz quiz;
  private User student;
  private Pageable pageable;

  @Autowired
  private StudentQuizRepository repository;

  @Before
  public void setUp() throws Exception {
    quiz = Quiz
        .builder()
        .id(QUIZ_ID)
        .title(QUIZ_TEXT)
        .build();

    student = User
        .builder()
        .id(USER_ID)
        .name(USER_NAME)
        .build();

    studentQuiz = StudentQuiz
        .builder()
        .id(STUDENT_QUIZ_ID)
        .quiz(quiz)
        .student(student)
        .trials(TRIALS)
        .done(DONE)
        .build();


    pageable = new PageRequest(0, 10);

    repository.save(studentQuiz);
  }

  @After
  public void tearDown() throws Exception {
    repository.deleteAll();
  }

  @Test
  public void testFindByIdAndDeletedFalse() {
    Optional<StudentQuiz> actual = repository.findByIdAndDeletedFalse(STUDENT_QUIZ_ID);

    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getTrials()).isEqualTo(studentQuiz.getTrials());
  }

  @Test
  public void testFindAllByStudentIdAndPageable() {
    Page<StudentQuiz> actual = repository.findAllByStudentId(USER_ID, pageable);

    assertThat(actual.getContent()).isNotEmpty();
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent().get(0).getQuiz()).isEqualTo(studentQuiz.getQuiz());
  }

  @Test
  public void testFindByStudentIdAndQuizId() {
    Optional<StudentQuiz> actual = repository.findByStudentIdAndQuizId(USER_ID, QUIZ_ID);

    assertThat(actual.get().getTrials()).isEqualTo(studentQuiz.getTrials());
  }
}