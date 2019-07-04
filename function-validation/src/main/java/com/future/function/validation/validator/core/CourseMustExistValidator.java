package com.future.function.validation.validator.core;

import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.validation.annotation.core.CourseMustExist;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CourseMustExistValidator
  implements ConstraintValidator<CourseMustExist, List<String>> {
  
  @Autowired
  private CourseRepository courseRepository;
  
  @Override
  public void initialize(CourseMustExist constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(
    List<String> courseIds, ConstraintValidatorContext context
  ) {
    
    return Optional.ofNullable(courseIds)
      .orElse(Collections.emptyList())
      .stream()
      .map(courseRepository::findOne)
      .allMatch(Objects::nonNull);
  }
  
}
