package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
  
  Course createCourse(Course course);
  
  Course updateCourse(Course course);
  
  Course getCourse(String courseId);
  Page<Course> getCourses(Pageable pageable);
  
  void deleteCourse(String courseId);
  
}
