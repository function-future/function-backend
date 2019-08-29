package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentQuizResponseMapperTest {

  private static final String STUDENT_QUIZ_ID = "id";

  private static final String QUIZ_ID = "quiz-id";

  private static final String STUDENT_ID = "student-id";

  private static final String BATCH_CODE = "batch-code";

  private static final Integer TRIALS = 1;

  private Quiz quiz;

  private Batch batch;

  private User student;

  private StudentQuiz studentQuiz;

  private Page<StudentQuiz> studentQuizPage;

  private Pageable pageable;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();

    student = User.builder()
      .id(STUDENT_ID)
      .build();

    quiz = Quiz.builder()
      .id(QUIZ_ID)
      .batch(batch)
      .trials(TRIALS + 1)
      .build();

    studentQuiz = StudentQuiz.builder()
      .id(STUDENT_QUIZ_ID)
      .quiz(quiz)
      .student(student)
      .trials(TRIALS)
      .build();

    pageable = new PageRequest(0, 10);

    studentQuizPage = new PageImpl<>(
      Collections.singletonList(studentQuiz), pageable, 1);
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void toStudentQuizWebResponse() {

    DataResponse<StudentQuizWebResponse> response =
      StudentQuizResponseMapper.toStudentQuizWebResponse(studentQuiz);
    assertThat(response.getData()
                 .getQuiz()
                 .getId()).isEqualTo(quiz.getId());
    assertThat(response.getData()
                 .getTrials()).isEqualTo(TRIALS);
  }

  @Test
  public void toPagingStudentQuizWebResponse() {

    PagingResponse<StudentQuizWebResponse> response =
      StudentQuizResponseMapper.toPagingStudentQuizWebResponse(studentQuizPage);
    assertThat(response.getPaging()
                 .getPage()).isEqualTo(1);
    assertThat(response.getPaging()
                 .getSize()).isEqualTo(10);
    assertThat(response.getPaging()
                 .getTotalRecords()).isEqualTo(1);
    assertThat(response.getData()
                 .get(0)
                 .getQuiz()
                 .getId()).isEqualTo(quiz.getId());
    assertThat(response.getData()
                 .get(0)
                 .getTrials()).isEqualTo(TRIALS);
  }

}
