package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for course logic operations declaration.
 */
public interface CourseService {
  
  /**
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
   * Deletes course object from database.
   *
   * @param courseId Id of course to be deleted.
   */
  void deleteCourse(String courseId);
  
}
