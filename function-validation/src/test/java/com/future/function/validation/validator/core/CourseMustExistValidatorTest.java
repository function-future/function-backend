package com.future.function.validation.validator.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.validation.annotation.core.CourseMustExist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseMustExistValidatorTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CourseMustExist annotation;

  @InjectMocks
  private CourseMustExistValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation, courseRepository);
  }

  @Test
  public void testGivenNullValueByValidatingFileMustExistReturnTrue() {

    assertThat(validator.isValid(null, null)).isTrue();

    verifyZeroInteractions(courseRepository);
  }

  @Test
  public void testGivenEmptyListByValidatingFileMustExistReturnTrue() {

    assertThat(validator.isValid(Collections.emptyList(), null)).isTrue();

    verifyZeroInteractions(courseRepository);
  }

  @Test
  public void testGivenListOfFileIdsByValidatingFileMustExistReturnTrue() {

    String courseId = "course-id";

    when(courseRepository.findOne(courseId)).thenReturn(new Course());

    assertThat(
      validator.isValid(Collections.singletonList(courseId), null)).isTrue();

    verify(courseRepository).findOne(courseId);
  }

  @Test
  public void testGivenListOfNonExistingFileIdsByValidatingFileMustExistReturnfLASE() {

    String courseId = "course-id";

    when(courseRepository.findOne(courseId)).thenReturn(null);

    assertThat(
      validator.isValid(Collections.singletonList(courseId), null)).isFalse();

    verify(courseRepository).findOne(courseId);
  }

}
