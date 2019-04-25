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
  
  /**
   * Converts {@code SharedCourseWebRequest} object to {@code List<Long>}
   * object containing batch numbers in the request object.
   *
   * @param request Object data (in form of {@code SharedCourseWebRequest})
   *                to be converted.
   *
   * @return {@code List<Long>} - Converted list object.
   */
  public List<Long> toCopyCoursesData(SharedCourseWebRequest request) {
    
    validator.validate(request);
    
    return request.getBatchNumbers();
  }
  
  /**
   * Converts JSON data to {@code Course} object.
   *
   * @param data JSON data (in form of String) to be converted.
   *
   * @return {@code Course} - Converted course object.
   */
  public Course toCourse(String data) {
    
    return toCourse(null, data);
  }
  
  /**
   * Converts JSON data to {@code Course} object. This method is used for
   * update course purposes.
   *
   * @param courseId Id of course to be updated.
   * @param data     JSON data (in form of String) to be converted.
   *
   * @return {@code Course} - Converted course object.
   */
  public Course toCourse(String courseId, String data) {
    
    return toValidatedCourse(courseId, requestMapper.toWebRequestObject(data, CourseWebRequest.class));
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
