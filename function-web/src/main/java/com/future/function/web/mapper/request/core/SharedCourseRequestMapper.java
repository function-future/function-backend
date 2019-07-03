package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.SharedCourseWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SharedCourseRequestMapper {
  
  private final CourseRequestMapper courseRequestMapper;
  
  private final RequestValidator validator;
  
  @Autowired
  public SharedCourseRequestMapper(
    CourseRequestMapper courseRequestMapper, RequestValidator validator
  ) {
    
    this.courseRequestMapper = courseRequestMapper;
    this.validator = validator;
  }
  
  public Course toCourse(String courseId, CourseWebRequest request) {
    
    return courseRequestMapper.toCourse(courseId, request);
  }
  
  public Pair<List<String>, String> toCourseIdsAndOriginBatchCodePair(
    SharedCourseWebRequest request
  ) {
    
    validator.validate(request);
    
    return Pair.of(request.getCourses(),
                   this.getOriginBatchOrEmptyString(request)
    );
  }
  
  private String getOriginBatchOrEmptyString(SharedCourseWebRequest request) {
    
    return Optional.of(request)
      .map(SharedCourseWebRequest::getOriginBatch)
      .orElse("");
  }
  
}
