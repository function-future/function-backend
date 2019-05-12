package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for course logic operations declaration.
 */
public interface CourseService {
  
  /**
   * @deprecated
   * Creates course object and saves any other data related to the
   * course.
   *
   * @param course Course data of new course.
   * @param file   File to be attached to course. May be null.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  Course createCourse(Course course, MultipartFile file);
  
  /**
   * Creates course object and saves any other data related to the
   * course.
   *
   * @param course Course data of new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  Course createCourse(Course course);
  
  /**
   * @deprecated
   * Updates course object and saves any other data related to the
   * course. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param course Course data of new course.
   * @param file   File to be attached to course. May be null.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  Course updateCourse(Course course, MultipartFile file);
  
  /**
   * Updates course object and saves any other data related to the
   * course. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param course Course data of new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  Course updateCourse(Course course);
  
  /**
   * Gets course based on specified courseId. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param courseId Id of course to be retrieved
   *
   * @return {@code Course} - The retrieved course object.
   */
  Course getCourse(String courseId);
  /**
   * Gets courses based on page parameters.
   *
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<Course>} - The retrieved courses object in Page.
   */
  Page<Course> getCourses(Pageable pageable);
  
  /**
   * Deletes course object from database.
   *
   * @param courseId Id of course to be deleted.
   */
  void deleteCourse(String courseId);
  
}
