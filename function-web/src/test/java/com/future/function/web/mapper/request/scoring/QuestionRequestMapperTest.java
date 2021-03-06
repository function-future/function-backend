package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.OptionWebRequest;
import com.future.function.web.model.request.scoring.QuestionWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionRequestMapperTest {

  private static final String QUESTION_TEXT = "question-label";

  private static final String QUESTION_ID = "question-id";

  private static final String OPTION_LABEL = "option-label";

  private Question question;

  private Option option;

  private QuestionWebRequest questionWebRequest;

  private OptionWebRequest optionWebRequest;

  @InjectMocks
  private QuestionRequestMapper requestMapper;

  @Mock
  private OptionRequestMapper optionRequestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() throws Exception {

    optionWebRequest = OptionWebRequest.builder()
      .label(OPTION_LABEL)
      .correct(true)
      .build();

    option = Option.builder()
      .label(OPTION_LABEL)
      .correct(true)
      .build();

    questionWebRequest = QuestionWebRequest.builder()
      .label(QUESTION_TEXT)
      .options(Collections.singletonList(optionWebRequest))
      .build();

    question = Question.builder()
      .label(QUESTION_TEXT)
      .options(Collections.singletonList(option))
      .build();

  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(validator, optionRequestMapper);
  }

  @Test
  public void toQuestion() {

    when(validator.validate(questionWebRequest)).thenReturn(questionWebRequest);
    when(optionRequestMapper.toOption(optionWebRequest)).thenReturn(option);

    Question actual = requestMapper.toQuestion(questionWebRequest);

    assertThat(actual.getOptions()
                 .get(0)
                 .getLabel()).isEqualTo(option.getLabel());
    assertThat(actual.getLabel()).isEqualTo(question.getLabel());

    verify(validator).validate(questionWebRequest);
    verify(optionRequestMapper).toOption(optionWebRequest);
  }

  @Test
  public void toQuestionNull() {

    catchException(() -> requestMapper.toQuestion(null));

    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
  }

  @Test
  public void toQuestionWithId() {

    when(validator.validate(questionWebRequest)).thenReturn(questionWebRequest);
    when(optionRequestMapper.toOption(optionWebRequest)).thenReturn(option);

    Question actual = requestMapper.toQuestion(questionWebRequest, QUESTION_ID);
    assertThat(actual.getLabel()).isEqualTo(question.getLabel());
    assertThat(actual.getOptions()
                 .get(0)
                 .getLabel()).isEqualTo(option.getLabel());

    verify(validator).validate(questionWebRequest);
    verify(optionRequestMapper).toOption(optionWebRequest);
  }

  @Test
  public void toQuestionIdNull() {

    when(validator.validate(questionWebRequest)).thenReturn(questionWebRequest);

    catchException(() -> requestMapper.toQuestion(questionWebRequest, null));

    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
    verify(validator).validate(questionWebRequest);
    verify(optionRequestMapper).toOption(optionWebRequest);
  }

}
