package com.future.function.validation.validator.scoring;

import com.future.function.repository.feature.scoring.QuestionBankRepository;
import com.future.function.validation.annotation.scoring.QuestionBankMustExist;
import java.util.Collections;
import javax.validation.ConstraintValidatorContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionBankMustExistValidatorTest {

  private static final String QUESTION_BANK_ID = "question-bank-id";

  private QuestionBankMustExist annotation;

  private ConstraintValidatorContext context;

  @Mock
  private QuestionBankRepository questionBankRepository;

  @InjectMocks
  private QuestionBankMustExistValidator validator;

  @Before
  public void setUp() throws Exception {
    when(questionBankRepository.existsByIdAndDeletedFalse(QUESTION_BANK_ID))
        .thenReturn(true);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(questionBankRepository);
  }

  @Test
  public void initialize() {
    validator.initialize(annotation);
  }

  @Test
  public void isValid() {
    boolean result = validator.isValid(Collections.singletonList(QUESTION_BANK_ID), context);
    assertThat(result).isTrue();
    verify(questionBankRepository).existsByIdAndDeletedFalse(QUESTION_BANK_ID);
  }

  @Test
  public void isNotValid() {
    boolean result = validator.isValid(Collections.singletonList("id"), context);
    assertThat(result).isFalse();
    verify(questionBankRepository).existsByIdAndDeletedFalse("id");
  }
}
