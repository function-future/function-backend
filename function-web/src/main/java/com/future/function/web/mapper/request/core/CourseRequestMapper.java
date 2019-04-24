package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CourseRequestMapper {
  
  private final WebRequestMapper requestMapper;
  
  private final ObjectValidator validator;
  
  @Autowired
  public CourseRequestMapper(
    ObjectValidator validator, WebRequestMapper requestMapper
  ) {
    
    this.validator = validator;
    this.requestMapper = requestMapper;
  }
  
  public List<Long> toCopyCoursesData(SharedCourseWebRequest request) {
    
    validator.validate(request);
    
    return request.getBatchNumbers();
  }
  
  public Course toCourse(String data) {
    
    return toCourse(null, data);
  }
  
  public Course toCourse(String courseId, String data) {
    
    return toValidatedCourse(
      courseId, requestMapper.toWebRequestObject(data, CourseWebRequest.class));
  }
  
  private Course toValidatedCourse(String courseId, CourseWebRequest request) {
    
    validator.validate(request);
    
    return Course.builder()
      .id(getOrNewCourseId(courseId))
      .title(request.getCourseTitle())
      .description(request.getCourseDescription())
      .build();
  }
  
  private String getOrNewCourseId(String courseId) {
    
    return Optional.ofNullable(courseId)
      .orElseGet(() -> UUID.randomUUID()
        .toString());
  }
  
}
