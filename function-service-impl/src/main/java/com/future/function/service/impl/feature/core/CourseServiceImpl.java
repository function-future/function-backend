package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.service.api.feature.core.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service implementation class for course logic operations implementation.
 */
@Service
public class CourseServiceImpl implements CourseService {
  
  private final CourseRepository courseRepository;
  
  @Autowired
  public CourseServiceImpl(CourseRepository courseRepository) {
    
    this.courseRepository = courseRepository;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course Course data of new course.
   * @param file   File to be attached to course. May be null.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course createCourse(Course course, MultipartFile file) {
    
    // TODO Store file
    return Optional.of(course)
      .map(courseRepository::save)
      .map(c -> this.getCourse(c.getId()))
      .orElseThrow(
        () -> new UnsupportedOperationException("Create Course Failed"));
  }
  
  private Course getCourse(String courseId) {
    
    return Optional.ofNullable(courseId)
      .map(courseRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course Course data of new course.
   * @param file   File to be attached to course. May be null.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course updateCourse(Course course, MultipartFile file) {
    
    // TODO Store file
    return Optional.of(course)
      .map(Course::getId)
      .map(courseRepository::findOne)
      .map(foundCourse -> copyPropertiesAndSaveCourse(course, foundCourse))
      .orElseThrow(
        () -> new UnsupportedOperationException("Update Course Failed"));
  }
  
  private Course copyPropertiesAndSaveCourse(
    Course course, Course foundCourse
  ) {
    
    BeanUtils.copyProperties(course, foundCourse,
                             FieldName.BaseEntity.CREATED_BY,
                             FieldName.BaseEntity.CREATED_AT,
                             FieldName.BaseEntity.VERSION
    );
    return courseRepository.save(foundCourse);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId Id of course to be deleted.
   */
  @Override
  public void deleteCourse(String courseId) {
    
    // TODO Delete file associated with course
    Optional.ofNullable(courseId)
      .ifPresent(courseRepository::delete);
  }
  
}
