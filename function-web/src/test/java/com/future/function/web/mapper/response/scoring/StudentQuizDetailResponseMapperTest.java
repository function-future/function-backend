package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.*;
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

  private StudentQuiz studentQuiz;
  private StudentQuizDetail studentQuizDetail;
  private StudentQuestion studentQuestion;
  private Question question;
  private Option option;

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
        .point(0)
        .build();

    option = Option
        .builder()
        .id(OPTION_ID)
        .label(OPTION_LABEL)
        .correct(false)
        .build();

    question = Question
        .builder()
        .id(QUESTION_ID)
            .label(QUESTION_TEXT)
        .options(Collections.singletonList(option))
        .build();

    studentQuestion = StudentQuestion
        .builder()
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
    DataResponse<StudentQuizDetailWebResponse> response = StudentQuizDetailResponseMapper
        .toStudentQuizDetailWebResponse(studentQuizDetail);
    assertThat(response.getData().getPoint()).isEqualTo(0);

  }

  @Test
  public void toStudentQuestionWebResponses() {
    PagingResponse<StudentQuestionWebResponse> responses = StudentQuizDetailResponseMapper
        .toStudentQuestionWebResponses(Collections.singletonList(studentQuestion));
    assertThat(responses.getData().get(0).getText()).isEqualTo(QUESTION_TEXT);
    assertThat(responses.getData().get(0).getOptions().get(0).getLabel()).isEqualTo(OPTION_LABEL);
    assertThat(responses.getData().get(0).getOptions().get(0).getCorrect()).isEqualTo(null);
  }
}