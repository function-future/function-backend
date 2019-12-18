package com.future.function.validation.validator.scoring;

import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.validation.annotation.scoring.QuizMustExist;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuizMustExistValidatorTest {

  private static final String QUIZ_ID = "quiz-id";

  private QuizMustExist annotation;

  private ConstraintValidatorContext context;

  @Mock
  private QuizRepository quizRepository;

  @InjectMocks
  private QuizMustExistValidator validator;

  @Before
  public void setUp() throws Exception {
    when(quizRepository.existsByIdAndDeletedFalse(QUIZ_ID)).thenReturn(true);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(quizRepository);
  }

  @Test
  public void initialize() {
    validator.initialize(annotation);
  }

  @Test
  public void isValid() {
    boolean result = validator.isValid(QUIZ_ID, context);
    assertThat(result).isTrue();
    verify(quizRepository).existsByIdAndDeletedFalse(QUIZ_ID);
  }

  @Test
  public void isNotValid() {
    boolean result = validator.isValid("id", context);
    assertThat(result).isFalse();
    verify(quizRepository).existsByIdAndDeletedFalse("id");
  }
}
