package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.CourseMustBeDistinct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CourseMustBeDistinctValidatorTest {

  @Mock
  private CourseMustBeDistinct annotation;

  @InjectMocks
  private CourseMustBeDistinctValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation);
  }

  @Test
  public void testGivenDistinctCourseIdsByValidatingCourseMustBeDistinctReturnTrue() {

    String courseId1 = "course-id-1";
    String courseId2 = "course-id-2";

    assertThat(
      validator.isValid(Arrays.asList(courseId1, courseId2), null)).isTrue();

    verifyZeroInteractions(annotation);
  }

  @Test
  public void testGivenIndistinctCourseIdsByValidatingCourseMustBeDistinctReturnFalse() {

    String courseId1 = "course-id-1";
    String courseId2 = "course-id-1";

    assertThat(
      validator.isValid(Arrays.asList(courseId1, courseId2), null)).isFalse();

    verifyZeroInteractions(annotation);
  }

  @Test
  public void testGivenNullCourseIdsByValidatingCourseMustBeDistinctReturnFalse() {

    assertThat(validator.isValid(null, null)).isFalse();

    verifyZeroInteractions(annotation);
  }

  @Test
  public void testGivenEmptyCourseIdsByValidatingCourseMustBeDistinctReturnFalse() {

    assertThat(validator.isValid(Collections.emptyList(), null)).isFalse();

    verifyZeroInteractions(annotation);
  }

}
