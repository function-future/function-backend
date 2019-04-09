package com.future.function.validation.validator.core;

import com.future.function.common.data.core.UserData;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Must implement {@link javax.validation.ConstraintValidator} interface.
 */
public class OnlyStudentCanHaveBatchAndUniversityValidator implements
  ConstraintValidator<OnlyStudentCanHaveBatchAndUniversity, UserData> {
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation - The annotation that uses this validator.
   */
  @Override
  public void initialize(
    OnlyStudentCanHaveBatchAndUniversity constraintAnnotation
  ) {
    // No initialization needed for this validator.
  }
  
  /**
   * Do logic for validation here
   *
   * @param value   - Value of data to be validated.
   * @param context - Context of annotation.
   *
   * @return {@code boolean} - Result of validation.
   */
  @Override
  public boolean isValid(UserData value, ConstraintValidatorContext context) {
    
    String role = value.getRoleAsString();
    
    if (role.equals("UNKNOWN")) {
      return false;
    }
    
    Long batchNumber = value.getBatchNumber();
    String university = value.getUniversity();
    
    if (role.equals("STUDENT")) {
      return batchNumber != null && university != null;
    } else {
      return batchNumber == null && university == null;
    }
  }
  
}
