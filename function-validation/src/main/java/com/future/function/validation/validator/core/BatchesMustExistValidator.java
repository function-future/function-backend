package com.future.function.validation.validator.core;

import com.future.function.common.data.core.CourseData;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.BatchesMustExist;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

/**
 * Validator of the {@link BatchesMustExist} annotation. Implements
 * {@link ConstraintValidator} interface with the second type being {@code
 * CourseData} as the validated data is implementation of {@code CourseData}
 * interface.
 */
public class BatchesMustExistValidator
  implements ConstraintValidator<BatchesMustExist, CourseData> {
  
  @Autowired
  private BatchRepository batchRepository;
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation The annotation that uses this validator.
   */
  @Override
  public void initialize(BatchesMustExist constraintAnnotation) {
    // No initialization needed for this validator.
  }
  
  /**
   * Checks whether the object of {@code SharingCourseData} contains existing
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
      .filter(this::eachHasBatchInDatabase)
      .isPresent();
  }
  
  private boolean eachHasBatchInDatabase(List<Long> batchNumbers) {
  
    return countFoundBatchInDatabase(batchNumbers) == batchNumbers.size();
  }
  
  private long countFoundBatchInDatabase(List<Long> batchNumbers) {
    
    return batchNumbers.stream()
      .filter(number -> !batchRepository.findByNumber(number)
        .equals(Optional.empty()))
      .count();
  }
  
}
