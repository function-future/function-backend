package com.future.function.validation.validator.scoring;

import com.future.function.validation.annotation.scoring.DateNotPassed;
import java.util.Date;
import javax.validation.ConstraintValidatorContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DateNotPassedValidatorTest {

  private static final Long date = new Date().getTime() + 100000000L;

  private DateNotPassed annotation;

  @Mock
  private ConstraintValidatorContext context;

  @InjectMocks
  private DateNotPassedValidator validator;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(context);
  }

  @Test
  public void initialize() {
    validator.initialize(annotation);
  }

  @Test
  public void isValid() {
    boolean result = validator.isValid(date, context);
    assertThat(result).isTrue();
  }

  @Test
  public void isNotValid() {
    boolean result = validator.isValid(new Date().getTime() - 150000L, context);
    assertThat(result).isFalse();
  }
}
