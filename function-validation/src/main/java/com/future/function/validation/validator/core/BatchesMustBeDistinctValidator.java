package com.future.function.validation.validator.core;

import com.future.function.common.data.core.CourseData;
import com.future.function.validation.annotation.core.BatchesMustBeDistinct;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * Validator of the {@link BatchesMustBeDistinct} annotation. Implements
 * {@link ConstraintValidator} interface with the second type being {@code
 * CourseData} as the validated data is implementation of {@code CourseData}
 * interface.
 */
public class BatchesMustBeDistinctValidator
  implements ConstraintValidator<BatchesMustBeDistinct, CourseData> {
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation The annotation that uses this validator.
   */
  @Override
  public void initialize(BatchesMustBeDistinct constraintAnnotation) {
    // No initialization needed for this validator.
  }
  
  /**
   * Checks whether the object of {@code SharedCourseData} contains duplicate
   * batch numbers or not.
   *
   * @param data    Value of data to be validated.
   * @param context Context of annotation.
   *
   * @return {@code boolean} - Result of validation.
   */
  @Override
  public boolean isValid(CourseData data, ConstraintValidatorContext context) {
    
    return Optional.of(data)
      .map(CourseData::getBatchNumbers)
      .filter(batches -> !batches.contains(null))
      .filter(this::hasNoDuplicateBatchNumbers)
      .isPresent();
  }
  
  private boolean hasNoDuplicateBatchNumbers(List<Long> batchNumbers) {
    
    long setSize = new LinkedHashSet<>(batchNumbers).size();
    return setSize == batchNumbers.size();
  }
  
}
