package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper class for incoming request for course feature.
 */
@Component
public class CourseRequestMapper {
  
  private final WebRequestMapper requestMapper;
  
  private final RequestValidator validator;
  
  @Autowired
  public CourseRequestMapper(
    RequestValidator validator, WebRequestMapper requestMapper
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
   * Converts JSON data to {@code Pair} of {@code Course} and {@code List<Long>}
   * object.
   *
   * @param data JSON data (in form of String) to be converted.
   *
   * @return {@code Pair} - Converted course object with its batch numbers.
   */
  public Pair<Course, List<Long>> toCourseAndBatchNumbers(String data) {
    
    return toCourseAndBatchNumbers(null, data);
  }
  
  /**
   * Converts JSON data to {@code Pair} of {@code Course} and {@code List<Long>}
   * object. This method is used for update course purposes.
   *
   * @param courseId Id of course to be updated.
   * @param data     JSON data (in form of String) to be converted.
   *
   * @return {@code Pair} - Converted course object with its batch numbers.
   */
  public Pair<Course, List<Long>> toCourseAndBatchNumbers(
    String courseId, String data
  ) {
    
    return toValidatedCourseAndBatchNumbers(courseId,
                                            requestMapper.toWebRequestObject(
                                              data, CourseWebRequest.class)
    );
  }
  
  private Pair<Course, List<Long>> toValidatedCourseAndBatchNumbers(
    String courseId, CourseWebRequest request
  ) {
    
    validator.validate(request);
    
    Course course = Course.builder()
      .title(request.getCourseTitle())
      .description(request.getCourseDescription())
      .build();
    
    if (courseId != null) {
      course.setId(courseId);
    }
    
    return Pair.of(course, request.getBatchNumbers());
  }
  
}
