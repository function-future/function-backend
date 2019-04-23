package com.future.function.service.api.feature.core.sharing;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.service.api.feature.core.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for course sharing logic operations declaration.
 */
public interface SharingCourseService extends CourseService {
  
  Course getCourse(String courseId, long batchNumber);
  
  Page<Course> getCourses(Pageable pageable, long batchNumber);
  
  void copyCourses(long originBatchNumber, long targetBatchNumber);
  
  Course createCourse(Course course, MultipartFile file, long batchNumber);
  
  Course updateCourse(Course course, MultipartFile file, long batchNumber);
  
}
