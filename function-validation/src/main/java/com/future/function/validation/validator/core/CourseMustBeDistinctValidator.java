package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.CourseMustBeDistinct;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CourseMustBeDistinctValidator
  implements ConstraintValidator<CourseMustBeDistinct, List<String>> {
  
  @Override
  public void initialize(CourseMustBeDistinct constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(
    List<String> courseIds, ConstraintValidatorContext context
  ) {
    
    return courseIds != null && this.isCourseIdsDistinct(courseIds);
  }
  
  private boolean isCourseIdsDistinct(List<String> courseIds) {
    
    return Optional.ofNullable(courseIds)
      .filter(ids -> !ids.isEmpty())
      .map(this::toSetOfCourseIds)
      .filter(set -> set.size() == courseIds.size())
      .isPresent();
  }
  
  private Set<String> toSetOfCourseIds(List<String> courseIds) {
    
    return Optional.of(courseIds)
      .map(HashSet::new)
      .orElseGet(HashSet::new);
  }
  
}
