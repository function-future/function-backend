package com.future.function.validation.validator;

import com.future.function.common.data.UserData;
import com.future.function.validation.annotation.OnlyStudentCanHaveBatchAndUniversity;

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
    Long batchNumber = value.getBatchNumber();
    String address = value.getUniversity();
    
    if (role.equals("STUDENT")) {
      return batchNumber != null && address != null;
    } else {
      return batchNumber == null && address == null;
    }
  }
  
}
