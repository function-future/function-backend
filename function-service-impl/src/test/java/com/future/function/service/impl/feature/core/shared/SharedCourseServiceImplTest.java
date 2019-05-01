package com.future.function.service.impl.feature.core.shared;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.shared.SharedCourse;
import com.future.function.repository.feature.core.shared.SharedCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.CourseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SharedCourseServiceImplTest {
  
  private static final String COURSE_ID = "course-id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final Course COURSE = Course.builder()
    .id(COURSE_ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .file(new File())
    .build();
  
  private static final Long ONE = 1L;
  
  private static final Batch BATCH_1 = new Batch(ONE);
  
  private static final String SHARED_COURSE_ID = "shared-course-id";
  
  private static final SharedCourse SHARED_COURSE = SharedCourse.builder()
    .id(SHARED_COURSE_ID)
    .course(COURSE)
    .batch(BATCH_1)
    .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 5);
  
  private static final List<SharedCourse> SHARED_COURSES =
    Collections.singletonList(SHARED_COURSE);
  
  private static final Page<SharedCourse> SHARED_COURSE_PAGE = new PageImpl<>(
    SHARED_COURSES, PAGEABLE, SHARED_COURSES.size());
  
  private static final List<Course> COURSES = Collections.singletonList(COURSE);
  
  private static final Page<Course> COURSE_PAGE = new PageImpl<>(
    COURSES, PAGEABLE, COURSES.size());
  
  private static final Long TWO = 2L;
  
  private static final Batch BATCH_2 = new Batch(TWO);
  
  @Mock
  private CourseService courseService;
  
  @Mock
  private BatchService batchService;
  
  @Mock
  private SharedCourseRepository sharedCourseRepository;
  
  @InjectMocks
  private SharedCourseServiceImpl sharedCourseService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(
      courseService, batchService, sharedCourseRepository);
  }
  
  @Test
  public void testGivenCourseIdAndBatchNumberByGettingCourseReturnCourseObject() {
    
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH_1
    )).thenReturn(Optional.of(SHARED_COURSE));
    
    Course retrievedCourse = sharedCourseService.getCourse(COURSE_ID, ONE);
    
    assertThat(retrievedCourse).isEqualTo(COURSE);
    
    verify(batchService).getBatch(ONE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH_1);
    verifyZeroInteractions(courseService);
  }
  
  @Test
  public void testGivenPageableAndBatchNumberByGettingCoursesReturnPageOfCourses() {
    
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    when(sharedCourseRepository.findAllByBatch(BATCH_1, PAGEABLE)).thenReturn(
      SHARED_COURSE_PAGE);
    
    Page<Course> retrievedCourses = sharedCourseService.getCourses(PAGEABLE,
                                                                   ONE
    );
    
    assertThat(retrievedCourses).isEqualTo(COURSE_PAGE);
    
    verify(batchService).getBatch(ONE);
    verify(sharedCourseRepository).findAllByBatch(BATCH_1, PAGEABLE);
    verifyZeroInteractions(courseService);
  }
  
  @Test
  public void testGivenCourseIdAndNoCourseUsageByDeletingCourseReturnSuccessfulDeletion() {
    
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH_1
    )).thenReturn(Optional.of(SHARED_COURSE));
    
    when(sharedCourseRepository.findAllByCourseId(COURSE_ID)).thenReturn(
      Stream.empty());
    
    sharedCourseService.deleteCourse(COURSE_ID, ONE);
    
    verify(batchService).getBatch(ONE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH_1);
    verify(sharedCourseRepository).findAllByCourseId(COURSE_ID);
    verify(sharedCourseRepository).delete(SHARED_COURSE);
    verify(courseService).deleteCourse(COURSE_ID);
  }
  
  @Test
  public void testGivenCourseIdAndCourseUsageExistsByDeletingCourseReturnSuccessfulDeletion() {
    
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH_1
    )).thenReturn(Optional.of(SHARED_COURSE));
    
    Stream<SharedCourse> sharedCourseStream = Stream.of(SHARED_COURSE);
    when(sharedCourseRepository.findAllByCourseId(COURSE_ID)).thenReturn(
      sharedCourseStream);
    
    sharedCourseService.deleteCourse(COURSE_ID, ONE);
    
    verify(batchService).getBatch(ONE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH_1);
    verify(sharedCourseRepository).findAllByCourseId(COURSE_ID);
    verify(sharedCourseRepository).delete(SHARED_COURSE);
    verifyZeroInteractions(courseService);
  }
  
  @Test
  public void testGivenTwoDistinctBatchNumbersByCopyingCoursesReturnSuccessfulCopying() {
    
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    
    Stream<SharedCourse> originSharedCourseStream = Stream.of(SHARED_COURSE);
    when(sharedCourseRepository.findAllByBatch(BATCH_1)).thenReturn(
      originSharedCourseStream);
    
    when(batchService.getBatch(TWO)).thenReturn(BATCH_2);
    
    SharedCourse savedSharedCourse = SharedCourse.builder()
      .course(COURSE)
      .batch(BATCH_2)
      .build();
    List<SharedCourse> savedSharedCourses = Collections.singletonList(
      savedSharedCourse);
    
    sharedCourseService.copyCourses(ONE, TWO);
    
    verify(batchService).getBatch(ONE);
    verify(sharedCourseRepository).findAllByBatch(BATCH_1);
    verify(batchService).getBatch(TWO);
    verify(sharedCourseRepository).save(savedSharedCourses);
  }
  
  @Test
  public void testGivenCourseAndMultipartFileAndBatchNumbersByCreatingCourseReturnCreatedCourseObject() {
    
    when(courseService.createCourse(COURSE, null)).thenReturn(COURSE);
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    
    List<Long> batchNumbers = Collections.singletonList(ONE);
    
    Course createdCourse = sharedCourseService.createCourse(
      COURSE, null, batchNumbers);
    
    assertThat(createdCourse).isEqualTo(COURSE);
    
    verify(courseService).createCourse(COURSE, null);
    verify(batchService).getBatch(ONE);
    
    SharedCourse savedSharedCourse = SharedCourse.builder()
      .course(COURSE)
      .batch(BATCH_1)
      .build();
    List<SharedCourse> sharedCourses = Collections.singletonList(
      savedSharedCourse);
    verify(sharedCourseRepository).save(sharedCourses);
  }
  
  @Test
  public void testGivenCourseAndMultipartFileAndBatchNumbersByUpdatingCourseReturnUpdatedCourseObject() {
    
    when(courseService.updateCourse(COURSE, null)).thenReturn(COURSE);
    when(batchService.getBatch(ONE)).thenReturn(BATCH_1);
    when(batchService.getBatch(TWO)).thenReturn(BATCH_2);
    
    Stream<SharedCourse> sharedCourseStream = Stream.of(SHARED_COURSE);
    when(sharedCourseRepository.findAllByCourseId(COURSE_ID)).thenReturn(
      sharedCourseStream);
    
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH_1
    )).thenReturn(Optional.of(SHARED_COURSE));
    
    List<Long> batchNumbers = Collections.singletonList(TWO);
    Course updatedCourse = sharedCourseService.updateCourse(COURSE, null,
                                                            batchNumbers
    );
    
    assertThat(updatedCourse).isEqualTo(COURSE);
    
    verify(courseService).updateCourse(COURSE, null);
    verify(batchService).getBatch(ONE);
    verify(batchService).getBatch(TWO);
    verify(sharedCourseRepository).findAllByCourseId(COURSE_ID);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH_1);
    verify(sharedCourseRepository).delete(SHARED_COURSE);
    
    SharedCourse savedSharedCourse = SharedCourse.builder()
      .course(COURSE)
      .batch(BATCH_2)
      .build();
    List<SharedCourse> savedSharedCourses = Collections.singletonList(
      savedSharedCourse);
    verify(sharedCourseRepository).save(savedSharedCourses);
  }
  
  @Test
  public void testGivenCourseAndMultipartFileAndExistingBatchNumbersByUpdatingCourseReturnUpdatedCourseObject() {
    
    when(courseService.updateCourse(COURSE, null)).thenReturn(COURSE);
    when(batchService.getBatch(TWO)).thenReturn(BATCH_2);
    
    Stream<SharedCourse> sharedCourseStream = Stream.of(SHARED_COURSE);
    when(sharedCourseRepository.findAllByCourseId(COURSE_ID)).thenReturn(
      sharedCourseStream);
    
    List<Long> batchNumbers = Arrays.asList(ONE, TWO);
    Course updatedCourse = sharedCourseService.updateCourse(
      COURSE, null, batchNumbers);
    
    assertThat(updatedCourse).isEqualTo(COURSE);
    
    verify(courseService).updateCourse(COURSE, null);
    verify(batchService).getBatch(TWO);
    verify(sharedCourseRepository).findAllByCourseId(COURSE_ID);
    
    SharedCourse savedSharedCourse = SharedCourse.builder()
      .course(COURSE)
      .batch(BATCH_2)
      .build();
    List<SharedCourse> savedSharedCourses = Collections.singletonList(
      savedSharedCourse);
    verify(sharedCourseRepository).save(savedSharedCourses);
  }
  
}
