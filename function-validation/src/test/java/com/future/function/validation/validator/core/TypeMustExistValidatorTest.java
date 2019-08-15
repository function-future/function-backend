package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.TypeMustExist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class TypeMustExistValidatorTest {

  @Mock
  private TypeMustExist annotation;

  @InjectMocks
  private TypeMustExistValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation);
  }

  @Test
  public void testGivenValidFileTypeByValidatingTypeMustBeValidFromFileWebRequestReturnTrue() {

    assertThat(validator.isValid("FILE", null)).isTrue();
    assertThat(validator.isValid("FOLDER", null)).isTrue();

    verifyZeroInteractions(annotation);
  }

  @Test
  public void testGivenInvalidFileTypeByValidatingTypeMustBeValidFromFileWebRequestReturnTrue() {

    assertThat(validator.isValid("SAMPLE", null)).isFalse();
    assertThat(validator.isValid(null, null)).isFalse();

    verifyZeroInteractions(annotation);
  }

}
