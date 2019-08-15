package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.NoSpace;
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
public class NoSpaceValidatorTest {

  @Mock
  private NoSpace annotation;

  @InjectMocks
  private NoSpaceValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation);
  }

  @Test
  public void testGivenNullValueByValidatingNoSpaceInStringReturnFalse() {

    assertThat(validator.isValid(null, null)).isFalse();
  }

  @Test
  public void testGivenStringWithSpaceByValidatingNoSpaceInStringReturnFalse() {

    assertThat(validator.isValid("Space ", null)).isFalse();
  }

  @Test
  public void testGivenStringWithNoSpaceByValidatingNoSpaceInStringReturnTrue() {

    assertThat(validator.isValid("NoSpace", null)).isTrue();
  }

}
