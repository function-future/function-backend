package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuestionWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizDetailWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentQuizDetailResponseMapperTest {

  private static final String STUDENT_QUIZ_ID = "student-quiz-id";

  private static final String STUDENT_QUIZ_DETAIL_ID = "student-quiz-detail-id";

  private static final String STUDENT_QUESTION_ID = "student-question-id";

  private static final String QUESTION_ID = "question-id";

  private static final String QUESTION_TEXT = "question-text";

  private static final String OPTION_ID = "option-id";

  private static final String OPTION_LABEL = "option-label";

  private static final Long TIME_LIMIT = 1000L;

  private Quiz quiz;

  private StudentQuiz studentQuiz;

  private StudentQuizDetail studentQuizDetail;

  private StudentQuestion studentQuestion;

  private Question question;

  private Option option;

  @Before
  public void setUp() throws Exception {

    quiz = Quiz.builder()
      .trials(10)
      .build();

    studentQuiz = StudentQuiz.builder()
      .id(STUDENT_QUIZ_ID)
      .quiz(quiz)
      .trials(5)
      .build();

    studentQuizDetail = StudentQuizDetail.builder()
      .id(STUDENT_QUIZ_DETAIL_ID)
      .studentQuiz(studentQuiz)
      .point(0)
      .build();

    option = Option.builder()
      .id(OPTION_ID)
      .label(OPTION_LABEL)
      .correct(false)
      .build();

    question = Question.builder()
      .id(QUESTION_ID)
      .label(QUESTION_TEXT)
      .options(Collections.singletonList(option))
      .build();

    studentQuestion = StudentQuestion.builder()
      .id(STUDENT_QUESTION_ID)
      .question(question)
      .option(option)
      .studentQuizDetail(studentQuizDetail)
      .number(1)
      .build();

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void toStudentQuizDetailWebResponse() {

    DataResponse<StudentQuizDetailWebResponse> response =
      StudentQuizDetailResponseMapper.toStudentQuizDetailWebResponse(
        studentQuizDetail);
    assertThat(response.getData()
                 .getPoint()).isEqualTo(0);
    assertThat(response.getData()
                .getTrials()).isEqualTo(5);

  }

  @Test
  public void toStudentQuestionWebResponses() {

    PagingResponse<StudentQuestionWebResponse> responses =
      StudentQuizDetailResponseMapper.toStudentQuestionWebResponses(
        Collections.singletonList(studentQuestion));
    assertThat(responses.getData()
                 .get(0)
                 .getText()).isEqualTo(QUESTION_TEXT);
    assertThat(responses.getData()
                 .get(0)
                 .getOptions()
                 .get(0)
                 .getLabel()).isEqualTo(OPTION_LABEL);
    assertThat(responses.getData()
                 .get(0)
                 .getOptions()
                 .get(0)
                 .getCorrect()).isEqualTo(null);
  }

  @Test
  public void toTimeLimitWebResponse() {
    DataResponse<Long> actual = StudentQuizDetailResponseMapper.toStudentQuestionTimeWebResponse(TIME_LIMIT);
    assertThat(actual.getData()).isEqualTo(TIME_LIMIT);
  }
}
