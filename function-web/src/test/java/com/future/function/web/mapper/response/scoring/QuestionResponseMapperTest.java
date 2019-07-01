package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.OptionWebResponse;
import com.future.function.web.model.response.feature.scoring.QuestionWebResponse;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;


public class QuestionResponseMapperTest {

  private static final String QUESTION_TEXT = "question-label";
  private static final String OPTION_LABEL = "option-label";

  private Question question;
  private Option option;
  private QuestionWebResponse questionWebResponse;
  private OptionWebResponse optionWebResponse;
  private Page<Question> questionPage;
  private Pageable pageable;

  @Before
  public void setUp() throws Exception {

    optionWebResponse = OptionWebResponse
        .builder()
        .label(OPTION_LABEL)
        .build();

    option = Option
        .builder()
        .label(OPTION_LABEL)
        .build();

    questionWebResponse = QuestionWebResponse
        .builder()
        .label(QUESTION_TEXT)
        .options(Collections.singletonList(optionWebResponse))
        .build();

    question = Question
        .builder()
        .label(QUESTION_TEXT)
        .options(Collections.singletonList(option))
        .build();

    pageable = new PageRequest(0, 10);

    questionPage = new PageImpl<>(Collections.singletonList(question), pageable, 10);

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void toQuestionWebResponse() {
    DataResponse<QuestionWebResponse> actual = QuestionResponseMapper.toQuestionWebResponse(question);
    assertThat(actual.getCode()).isEqualTo(200);
    assertThat(actual.getData().getLabel()).isEqualTo(QUESTION_TEXT);
    assertThat(actual.getData().getOptions().get(0).getLabel()).isEqualTo(OPTION_LABEL);
  }

  @Test
  public void toQuestionWebResponseCreated() {
    DataResponse<QuestionWebResponse> actual = QuestionResponseMapper.toQuestionWebResponse(HttpStatus.CREATED, question);
    assertThat(actual.getCode()).isEqualTo(201);
    assertThat(actual.getData().getLabel()).isEqualTo(QUESTION_TEXT);
    assertThat(actual.getData().getOptions().get(0).getLabel()).isEqualTo(OPTION_LABEL);
  }

  @Test
  public void toQuestionPagingResponse() {
    PagingResponse<QuestionWebResponse> actual = QuestionResponseMapper.toQuestionPagingResponse(questionPage);

    assertThat(actual.getCode()).isEqualTo(200);
    assertThat(actual.getData().get(0).getLabel()).isEqualTo(QUESTION_TEXT);
    assertThat(actual.getData().get(0).getOptions().get(0).getLabel()).isEqualTo(OPTION_LABEL);
  }
}
