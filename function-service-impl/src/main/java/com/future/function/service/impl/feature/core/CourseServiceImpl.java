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

@Service
public class CourseServiceImpl implements CourseService {
  
  private final CourseRepository courseRepository;
  
  @Autowired
  public CourseServiceImpl(CourseRepository courseRepository) {
    
    this.courseRepository = courseRepository;
  }
  
  @Override
  public Course createCourse(Course course, MultipartFile file) {
  
    // TODO Store file
    courseRepository.save(course);
  
    return courseRepository.findOne(course.getId());
  }
  
  @Override
  public Course updateCourse(Course course, MultipartFile file) {
  
    // TODO Store file
    return Optional.of(course.getId())
      .map(courseRepository::findOne)
      .map(foundCourse -> copyPropertiesAndSaveCourse(course, foundCourse))
      .orElseThrow(() -> new NotFoundException("Update Course Not Found"));
  }
  
  private Course copyPropertiesAndSaveCourse(
    Course course, Course foundCourse
  ) {
    
    BeanUtils.copyProperties(course, foundCourse,
                             FieldName.BaseEntity.CREATED_BY,
                             FieldName.BaseEntity.CREATED_AT
    );
    return courseRepository.save(foundCourse);
  }
  
  @Override
  public void deleteCourse(String courseId) {
  
    // TODO Delete file associated with course
    Optional.ofNullable(courseId)
      .ifPresent(courseRepository::delete);
  }
  
}
