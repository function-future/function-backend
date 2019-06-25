package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Name;
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
public class NameValidatorTest {

  private static final String VALID_NAME = "Name Name";

  private static final String INVALID_NAME = "Name 1";

  @Mock
  private Name annotation;

  @InjectMocks
  private NameValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation);
  }

  @Test
  public void testGivenValidStringAsNameByValidatingNameReturnTrue() {

    assertThat(validator.isValid(VALID_NAME, null)).isTrue();
  }

  @Test
  public void testGivenValidStringAsNameByValidatingNameReturnFalse() {

    assertThat(validator.isValid(INVALID_NAME, null)).isFalse();
  }

  @Test
  public void testGivenNullStringAsNameByValidatingNameReturnFalse() {

    assertThat(validator.isValid(null, null)).isFalse();
  }

}
