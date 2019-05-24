package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.CourseWebRequestV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper class for incoming request for course feature.
 */
@Component
public class CourseRequestMapperV2 {
  
  private final RequestValidator validator;
  
  @Autowired
  public CourseRequestMapperV2(
    RequestValidator validator
  ) {
    
    this.validator = validator;
  }
  
  /**
   * Converts JSON data to {@code Course} object. This method is used for
   * update course purposes.
   *
   * @param request  JSON data (in form of CourseWebRequestV2) to be converted.
   *
   * @return {@code Course} - Converted course object.
   */
  public Course toCourse(CourseWebRequestV2 request) {
    
    validator.validate(request);
    
    return Course.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .file(this.getFileV2(request))
      .build();
  }
  
  private FileV2 getFileV2(CourseWebRequestV2 request) {
  
    return Optional.of(request)
      .map(CourseWebRequestV2::getMaterial)
      .map(list -> list.get(0))
      .map(this::buildFileV2)
      .orElseGet(FileV2::new);
  }
  
  private FileV2 buildFileV2(String fileId) {
    
    return FileV2.builder()
      .id(fileId)
      .build();
  }
  
  /**
   * Converts JSON data to {@code Course} object. This method is used for
   * update course purposes.
   *
   * @param courseId Id of course to be updated.
   * @param request  JSON data (in form of CourseWebRequestV2) to be converted.
   *
   * @return {@code Course} - Converted course object.
   */
  public Course toCourse(String courseId, CourseWebRequestV2 request) {
    
    validator.validate(request);
    
    Course course = Course.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .file(this.getFileV2(request))
      .build();
    
    if (courseId != null) {
      course.setId(courseId);
    }
    
    return course;
  }
  
}
