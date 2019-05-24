package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.service.api.feature.core.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Service implementation class for course logic operations implementation.
 */
@Service
public class CourseServiceImpl implements CourseService {
  
  private final CourseRepository courseRepository;
  
  private final ResourceService resourceService;
  
  @Autowired
  public CourseServiceImpl(
    CourseRepository courseRepository, ResourceService resourceService
  ) {
    
    this.courseRepository = courseRepository;
    this.resourceService = resourceService;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course Course data of new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course createCourse(Course course) {
    
    return Optional.of(course)
      .map(this::setCourseFile)
      .map(courseRepository::save)
      .map(c -> this.getCourse(c.getId()))
      .orElseThrow(() -> new UnsupportedOperationException("Create Course Failed"));
  }
  
  private Course setCourseFile(Course course) {
    
    return Optional.of(course)
      .map(Course::getFile)
      .map(FileV2::getId)
      .map(fileId -> this.markAndSetCourseFile(course, fileId, true))
      .orElse(course);
  }
  
  private Course markAndSetCourseFile(
    Course course, String fileId, boolean used
  ) {
    
    resourceService.markFilesUsed(Collections.singletonList(fileId), used);
    course.setFile(resourceService.getFile(fileId));
    return course;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course Course data of new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course updateCourse(Course course) {
    
    return Optional.of(course)
      .map(Course::getId)
      .map(courseRepository::findOne)
      .map(this::deleteCourseFile)
      .map(foundCourse -> this.setCourseFile(course, foundCourse))
      .map(foundCourse -> this.copyPropertiesAndSaveCourse(course, foundCourse))
      .orElse(course);
  }
  
  private Course setCourseFile(Course course, Course foundCourse) {
    
    return Optional.of(course)
      .map(Course::getFile)
      .map(FileV2::getId)
      .map(fileId -> this.markAndSetCourseFile(course, fileId, true))
      .orElse(course);
  }
  
  private Course deleteCourseFile(Course course) {
    
    return Optional.of(course)
      .map(Course::getFile)
      .map(FileV2::getId)
      .map(id -> this.markAndSetCourseFile(course, id, false))
      .orElse(course);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId Id of course to be retrieved
   *
   * @return {@code Course} - The retrieved course object.
   */
  @Override
  public Course getCourse(String courseId) {
    
    return Optional.ofNullable(courseId)
      .map(courseRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }
  
  @Override
  public Page<Course> getCourses(Pageable pageable) {
    
    return courseRepository.findAll(pageable);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId Id of course to be deleted.
   */
  @Override
  public void deleteCourse(String courseId) {
    
    Optional.ofNullable(courseId)
      .map(courseRepository::findOne)
      .ifPresent(course -> {
        this.deleteCourseFile(course);
        courseRepository.delete(course);
      });
  }
  
  private Course copyPropertiesAndSaveCourse(
    Course course, Course foundCourse
  ) {
    
    BeanUtils.copyProperties(course, foundCourse);
    return courseRepository.save(foundCourse);
  }
  
}
