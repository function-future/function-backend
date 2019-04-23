package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for course logic operations declaration.
 */
public interface CourseService {
  
  Course createCourse(Course course, MultipartFile file);
  
  Course updateCourse(Course course, MultipartFile file);
  
  void deleteCourse(String courseId);
  
}
