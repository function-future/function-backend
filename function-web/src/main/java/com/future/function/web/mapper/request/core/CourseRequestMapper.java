package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.CourseWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public CourseRequestMapper(
    RequestValidator validator
  ) {

    this.validator = validator;
  }

  public Course toCourse(CourseWebRequest request) {

    return toCourse(null, request);
  }

  public Course toCourse(String courseId, CourseWebRequest request) {

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

  private FileV2 getFileV2(CourseWebRequest request) {

    return Optional.of(request)
      .map(CourseWebRequest::getMaterial)
      .filter(materialList -> !materialList.isEmpty())
      .map(list -> list.get(0))
      .map(this::buildFileV2)
      .orElseGet(FileV2::new);
  }

  private FileV2 buildFileV2(String fileId) {

    return FileV2.builder()
      .id(fileId)
      .build();
  }

}
