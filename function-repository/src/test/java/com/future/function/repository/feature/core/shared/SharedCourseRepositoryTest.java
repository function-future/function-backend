package com.future.function.repository.feature.core.shared;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.shared.SharedCourse;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.repository.feature.core.CourseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class SharedCourseRepositoryTest {
  
  @Autowired
  private SharedCourseRepository sharedCourseRepository;
  
  @Autowired
  private CourseRepository courseRepository;
  
  @Autowired
  private BatchRepository batchRepository;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    sharedCourseRepository.deleteAll();
    courseRepository.deleteAll();
    batchRepository.deleteAll();
  }
  
  @Test
  public void testGivenCourseIdAndBatchByFindingByCourseIdAndBatchReturnSharedCourseObject() {
    
    String courseId = "course-id-1";
    courseRepository.save(buildCourse(courseId));
    
    long number = 1L;
    batchRepository.save(new Batch(number));
    
    SharedCourse sharedCourse = buildSharedCourse(courseId, number);
    sharedCourseRepository.save(sharedCourse);
    
    Batch foundBatch = getBatch(number);
    
    Optional<SharedCourse> foundSharedCourse =
      sharedCourseRepository.findByCourseIdAndBatch(courseId, foundBatch);
    
    assertThat(foundSharedCourse.isPresent()).isTrue();
    assertThat(foundSharedCourse).isEqualTo(Optional.of(sharedCourse));
  }
  
  private SharedCourse buildSharedCourse(String courseId, long number) {
    
    return SharedCourse.builder()
      .course(courseRepository.findOne(courseId))
      .batch(getBatch(number))
      .build();
  }
  
  private Batch getBatch(long number) {
    
    return batchRepository.findByNumber(number)
      .get();
  }
  
  private Course buildCourse(String courseId) {
    
    return Course.builder()
      .id(courseId)
      .build();
  }
  
  @Test
  public void testGivenBatchByFindingByBatchReturnPageOfSharedCourses() {
    
    String courseId = "course-id-2";
    courseRepository.save(buildCourse(courseId));
    
    long number = 2L;
    batchRepository.save(new Batch(number));
    
    SharedCourse sharedCourse = buildSharedCourse(courseId, number);
    sharedCourseRepository.save(sharedCourse);
    
    Batch foundBatch = getBatch(number);
    
    Pageable pageable = new PageRequest(0, 2);
    
    Page<SharedCourse> foundSharedCoursePage =
      sharedCourseRepository.findAllByBatch(foundBatch, pageable);
    
    Page<SharedCourse> sharedCoursePage = new PageImpl<>(
      Collections.singletonList(sharedCourse), pageable, 1);
    
    assertThat(foundSharedCoursePage).isNotNull();
    assertThat(foundSharedCoursePage).isEqualTo(sharedCoursePage);
  }
  
  @Test
  public void testGivenBatchByFindingByBatchReturnStreamOfSharedCourses() {
  
    String courseId = "course-id-3";
    courseRepository.save(buildCourse(courseId));
  
    long number = 3L;
    batchRepository.save(new Batch(number));
  
    SharedCourse sharedCourse = buildSharedCourse(courseId, number);
    sharedCourseRepository.save(sharedCourse);
  
    Batch foundBatch = getBatch(number);
  
    Stream<SharedCourse> foundSharedCourseStream =
      sharedCourseRepository.findAllByBatch(foundBatch);
  
    Stream<SharedCourse> sharedCourseStream = Stream.of(sharedCourse);
  
    assertThat(foundSharedCourseStream).isNotEqualTo(Stream.empty());
    assertThat(foundSharedCourseStream.collect(Collectors.toList())).isEqualTo(
      sharedCourseStream.collect(Collectors.toList()));
  }
  
  @Test
  public void testGivenCourseIdByFindingByCourseIdReturnStreamOfSharedCourses() {
    
    String courseId = "course-id-4";
    courseRepository.save(buildCourse(courseId));
    
    long number = 4L;
    batchRepository.save(new Batch(number));
    
    SharedCourse sharedCourse = buildSharedCourse(courseId, number);
    sharedCourseRepository.save(sharedCourse);
    
    Stream<SharedCourse> foundSharedCourseStream =
      sharedCourseRepository.findAllByCourseId(courseId);
    
    Stream<SharedCourse> sharedCourseStream = Stream.of(sharedCourse);
    
    assertThat(foundSharedCourseStream).isNotEqualTo(Stream.empty());
    assertThat(foundSharedCourseStream.collect(Collectors.toList())).isEqualTo(
      sharedCourseStream.collect(Collectors.toList()));
  }
  
}