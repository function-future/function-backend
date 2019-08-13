package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.QuestionBankWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionBankRequestMapperTest {

  private static final String QUESTION_BANK_DESCRIPTION =
    "questionbank-description";

  private static final String QUESTION_BANK_ID = "questionbank-id";

  @InjectMocks
  private QuestionBankRequestMapper requestMapper;

  @Mock
  private RequestValidator validator;

  private QuestionBankWebRequest webRequest;

  private QuestionBank questionBank;

  @Before
  public void setUp() throws Exception {

    webRequest = QuestionBankWebRequest.builder()
      .description(QUESTION_BANK_DESCRIPTION)
      .build();

    questionBank = QuestionBank.builder()
      .id(QUESTION_BANK_ID)
      .description(QUESTION_BANK_DESCRIPTION)
      .build();

    when(validator.validate(webRequest)).thenReturn(webRequest);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testToQuestionBank() {

    questionBank.setId(null);
    QuestionBank actual = requestMapper.toQuestionBank(webRequest);
    assertThat(actual.getDescription()).isEqualTo(
      questionBank.getDescription());
    verify(validator).validate(webRequest);
  }

  @Test
  public void testToQuestionBankWithId() {

    QuestionBank actual = requestMapper.toQuestionBank(
      QUESTION_BANK_ID, webRequest);
    assertThat(actual).isEqualTo(questionBank);
    verify(validator).validate(webRequest);
  }

  @Test
  public void testToQuestionBankBadRequestException() {

    catchException(() -> requestMapper.toQuestionBank(null));
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
  }

}
