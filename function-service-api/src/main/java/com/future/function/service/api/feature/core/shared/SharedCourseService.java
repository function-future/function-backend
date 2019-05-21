package com.future.function.service.api.feature.core.shared;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface class for shared course logic operations declaration.
 */
public interface SharedCourseService {
  
  /**
   * Retrieves an course from database given the course's id. If
   * not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param courseId    Id of course to be retrieved.
   * @param batchCode Batch number of current user (obtained from session).
   *
   * @return {@code Course} - The course object found in database.
   */
  Course getCourse(String courseId, String batchCode);
  
  /**
   * Retrieves courses from database.
   *
   * @param pageable  Pageable object for paging data.
   * @param batchCode Batch number of current user (obtained from session).
   *
   * @return {@code Page<Course>} - Page of courses found in database.
   */
  Page<Course> getCourses(Pageable pageable, String batchCode);
  
  /**
   * Creates course object and saves any other data related to the course.
   *
   * @param course     Course data of new course.
   * @param file       File to be attached to course. May be null.
   * @param batchCodes Batch numbers of the new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  Course createCourse(
    Course course, MultipartFile file, List<String> batchCodes
  );
  
  /**
   * Updates course object and saves any other data related to the
   * course. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param course     Course data of new course.
   * @param file       File to be attached to course. May be null.
   * @param batchCodes Batch numbers of the new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  Course updateCourse(
    Course course, MultipartFile file, List<String> batchCodes
  );
  
  /**
   * Deletes course object from database.
   *
   * @param courseId  Id of course to be deleted.
   * @param batchCode Batch number of selected course.
   */
  void deleteCourse(String courseId, String batchCode);
  
  /**
   * Copies course objects of {@code originBatchCode} by creating new
   * {@link com.future.function.model.entity.feature.core.shared.SharedCourse}
   * objects linking to {@code targetBatchCode}.
   *
   * @param originBatchCode Batch number of origin courses.
   * @param targetBatchCode Targeted batch number for courses to be copied.
   */
  void copyCourses(String originBatchCode, String targetBatchCode);
  
}
