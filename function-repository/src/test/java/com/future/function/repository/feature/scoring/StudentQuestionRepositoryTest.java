package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentQuestionRepositoryTest {

  private static final String STUDENT_QUIZ_DETAIL_ID = "detail-id";

  private Sort sort;
  private StudentQuestion studentQuestion;

  @Autowired
  private StudentQuestionRepository studentQuestionRepository;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void findAllByStudentQuizDetailId() {

    Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "number");

    StudentQuizDetail detail = StudentQuizDetail.builder().id(STUDENT_QUIZ_DETAIL_ID).build();

    studentQuestion = StudentQuestion.builder().number(1).correct(true).studentQuizDetail(detail).build();
    studentQuestionRepository.save(studentQuestion);

    List<StudentQuestion> actual = studentQuestionRepository
        .findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(STUDENT_QUIZ_DETAIL_ID);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getNumber()).isEqualTo(1);

  }
}
