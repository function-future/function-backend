package com.future.function.service.api.feature.core.shared;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for shared course logic operations declaration.
 */
public interface SharedCourseService {
  
  Course getCourse(String courseId, long batchNumber);
  
  Page<Course> getCourses(Pageable pageable, long batchNumber);
  
  void copyCourses(long originBatchNumber, long targetBatchNumber);
  
  Course createCourse(Course course, MultipartFile file, long batchNumber);
  
  Course updateCourse(Course course, MultipartFile file, long batchNumber);
  
  void deleteCourse(String courseId, long batchNumber);
  
}
