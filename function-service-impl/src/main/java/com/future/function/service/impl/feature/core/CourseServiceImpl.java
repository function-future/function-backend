package com.future.function.service.impl.feature.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.service.api.feature.core.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
  
  private final CourseRepository courseRepository;
  
  @Autowired
  public CourseServiceImpl(CourseRepository courseRepository) {
    
    this.courseRepository = courseRepository;
  }
  
  @Override
  public Course createCourse(Course course, MultipartFile file) {
    
    return courseRepository.save(course);
  }
  
  @Override
  public Course updateCourse(Course course, MultipartFile file) {
    
    return null;
  }
  
  @Override
  public void deleteCourse(String courseId) {
    
    Optional.ofNullable(courseId)
      .ifPresent(courseRepository::delete);
  }
  
}
