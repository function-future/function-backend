package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.web.model.request.core.SharingCourseWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseRequestMapper {
  
  private final ObjectValidator validator;
  
  @Autowired
  public CourseRequestMapper(ObjectValidator validator) {
    
    this.validator = validator;
  }
  
  public List<Long> toCopyCoursesData(SharingCourseWebRequest request) {
    
    validator.validate(request);
    
    return request.getBatchNumbers();
  }
  
}
