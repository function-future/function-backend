package com.future.function.validation.validator.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.SharedCourse;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.repository.feature.core.SharedCourseRepository;
import com.future.function.validation.annotation.core.CourseMustExist;
import com.future.function.validation.dummy.SharedCourseDummyData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseMustExistValidatorTest {

  private static final String COURSE_ID = "course-id";

  private static final String SHARED_COURSE_ID = "shared-course-id";

  private static final String BATCH_CODE = "batch-code";

  private static final Batch BATCH = Batch.builder()
    .code(BATCH_CODE)
    .build();

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private SharedCourseRepository sharedCourseRepository;

  @Mock
  private BatchRepository batchRepository;

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

    verifyNoMoreInteractions(
      annotation, courseRepository, sharedCourseRepository, batchRepository);
  }

  @Test
  public void testGivenEmptyListAndNullOriginBatchByValidatingCourseMustExistReturnTrue() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      null, null, Collections.emptyList());

    assertThat(validator.isValid(data, null)).isTrue();

    verifyZeroInteractions(
      courseRepository, sharedCourseRepository, batchRepository);
  }

  @Test
  public void testGivenEmptyListAndNotNullOriginBatchByValidatingCourseMustExistReturnTrue() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      BATCH_CODE, null, Collections.emptyList());

    when(batchRepository.findByCodeAndDeletedFalse(BATCH_CODE)).thenReturn(
      Optional.of(BATCH));

    assertThat(validator.isValid(data, null)).isTrue();

    verify(batchRepository).findByCodeAndDeletedFalse(BATCH_CODE);

    verifyZeroInteractions(courseRepository, sharedCourseRepository);
  }

  @Test
  public void testGivenMasterCourseIdAndNonNullOriginBatchByValidatingCourseMustExistReturnFalse() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      BATCH_CODE, null, Collections.singletonList(COURSE_ID));

    when(batchRepository.findByCodeAndDeletedFalse(BATCH_CODE)).thenReturn(
      Optional.of(BATCH));
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.empty());

    assertThat(validator.isValid(data, null)).isFalse();

    verify(batchRepository).findByCodeAndDeletedFalse(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);

    verifyZeroInteractions(courseRepository);
  }

  @Test
  public void testGivenMasterCourseIdAndNullOriginBatchByValidatingCourseMustExistReturnTrue() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      null, null, Collections.singletonList(COURSE_ID));

    Course course = Course.builder()
      .id(COURSE_ID)
      .build();

    when(courseRepository.findByIdAndDeletedFalse(COURSE_ID)).thenReturn(
      Optional.of(course));

    assertThat(validator.isValid(data, null)).isTrue();

    verify(courseRepository).findByIdAndDeletedFalse(COURSE_ID);

    verifyZeroInteractions(sharedCourseRepository, batchRepository);
  }

  @Test
  public void testGivenSharedCourseIdAndNullOriginBatchByValidatingCourseMustExistReturnFalse() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      null, null, Collections.singletonList(SHARED_COURSE_ID));

    when(courseRepository.findByIdAndDeletedFalse(SHARED_COURSE_ID)).thenReturn(
      Optional.empty());

    assertThat(validator.isValid(data, null)).isFalse();

    verify(courseRepository).findByIdAndDeletedFalse(SHARED_COURSE_ID);

    verifyZeroInteractions(sharedCourseRepository, batchRepository);
  }

  @Test
  public void testGivenSharedCourseIdAndNonNullAndDeletedOriginBatchByValidatingCourseMustExistReturnFalse() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      BATCH_CODE, null, Collections.singletonList(COURSE_ID));

    when(batchRepository.findByCodeAndDeletedFalse(BATCH_CODE)).thenReturn(
      Optional.empty());

    assertThat(validator.isValid(data, null)).isFalse();

    verify(batchRepository).findByCodeAndDeletedFalse(BATCH_CODE);

    verifyZeroInteractions(courseRepository, sharedCourseRepository);
  }

  @Test
  public void testGivenSharedCourseIdAndNonNullAndNonDeletedOriginBatchByValidatingCourseMustExistReturnTrue() {

    SharedCourseDummyData data = new SharedCourseDummyData(
      BATCH_CODE, null, Collections.singletonList(COURSE_ID));

    Course course = Course.builder()
      .id(COURSE_ID)
      .build();

    SharedCourse sharedCourse = SharedCourse.builder()
      .id(SHARED_COURSE_ID)
      .batch(BATCH)
      .course(course)
      .build();

    when(batchRepository.findByCodeAndDeletedFalse(BATCH_CODE)).thenReturn(
      Optional.of(BATCH));
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.of(sharedCourse));

    assertThat(validator.isValid(data, null)).isTrue();

    verify(batchRepository).findByCodeAndDeletedFalse(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);

    verifyZeroInteractions(courseRepository);
  }

}
