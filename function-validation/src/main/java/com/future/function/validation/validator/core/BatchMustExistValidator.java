package com.future.function.validation.validator.core;

import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.BatchMustExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Validator of the {@link BatchMustExist} annotation. Implements
 * {@link ConstraintValidator} interface with the second type being {@code
 * String} as the validated data is batch code in request.
 */
public class BatchMustExistValidator
  implements ConstraintValidator<BatchMustExist, String> {
  
  @Autowired
  private BatchRepository batchRepository;
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation The annotation that uses this validator.
   */
  @Override
  public void initialize(BatchMustExist constraintAnnotation) {
    // No initialization needed for this validator.
  }
  
  /**
   * Checks whether the object of {@code SharedCourseData} contains existing
   * batch numbers or not.
   *
   * @param batchCode Value of batchCode to be validated.
   * @param context   Context of annotation.
   *
   * @return {@code boolean} - Result of validation.
   */
  @Override
  public boolean isValid(String batchCode, ConstraintValidatorContext context) {
    
    if (batchCode == null || StringUtils.isEmpty(batchCode)) {
      return true;
    }
    
    return Optional.of(batchCode)
      .filter(this::isBatchWithBatchCodeExist)
      .isPresent();
  }
  
  private boolean isBatchWithBatchCodeExist(String batchCode) {
    
    return !batchRepository.findByCode(batchCode)
      .equals(Optional.empty());
  }
  
}
