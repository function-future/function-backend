package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class CourseRepositoryTest {

  @Autowired
  private CourseRepository courseRepository;

  @Before
  public void setUp() {}

  @After
  public void tearDown() {

    courseRepository.deleteAll();
  }

  @Test
  public void testGivenCourseIdByFindingCourseByIdAndDeletedFalseReturnCourseObject() {

    Course course = Course.builder()
      .title("course-title")
      .description("course-description")
      .build();
    course.setDeleted(false);
    courseRepository.save(course);

    Optional<Course> foundCourse = courseRepository.findByIdAndDeletedFalse(
      course.getId());

    assertThat(foundCourse.isPresent()).isTrue();
    assertThat(foundCourse).isEqualTo(Optional.of(course));
  }

  @Test
  public void testGivenCourseIdAndDeletedCourseByFindingCourseByIdAndDeletedFalseReturnEmpty() {

    Course course = Course.builder()
      .title("course-title")
      .description("course-description")
      .build();
    course.setDeleted(true);
    courseRepository.save(course);

    Optional<Course> foundCourse = courseRepository.findByIdAndDeletedFalse(
      course.getId());

    assertThat(foundCourse.isPresent()).isFalse();
  }

  @Test
  public void testGivenCoursesByFindingCoursesReturnPageOfCourse() {

    Course course1 = Course.builder()
      .title("course-title")
      .description("course-description")
      .build();
    course1.setDeleted(false);
    course1.setUpdatedAt(1L);
    courseRepository.save(course1);

    Course course2 = Course.builder()
      .title("course-title")
      .description("course-description")
      .build();
    course2.setDeleted(false);
    course2.setUpdatedAt(2L);
    courseRepository.save(course2);

    Page<Course> foundCourses = courseRepository.findAllByOrderByUpdatedAtDesc(
      new PageRequest(0, 10));

    assertThat(foundCourses.getContent()).isNotEmpty();
    assertThat(foundCourses.getContent()).isEqualTo(
      Arrays.asList(course2, course1));
  }

}
