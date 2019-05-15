package com.future.function.validation.validator.core;

import com.future.function.common.data.core.UserData;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator of the
 * {@link com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity} annotation.
 * Implements  {@link javax.validation.ConstraintValidator} interface with
 * the second type being {@link com.future.function.common.data.core.UserData}
 * interface as the validated data is implementation of the interface.
 */
public class OnlyStudentCanHaveBatchAndUniversityValidator implements
  ConstraintValidator<OnlyStudentCanHaveBatchAndUniversity, UserData> {
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation The annotation that uses this validator.
   */
  @Override
  public void initialize(
    OnlyStudentCanHaveBatchAndUniversity constraintAnnotation
  ) {
    // No initialization needed for this validator.
  }
  
  /**
   * Checks whether the given implementation of the
   * {@link com.future.function.common.data.core.UserData} interface has
   * valid fields given its role.
   *
   * @param value   Value of data to be validated.
   * @param context Context of annotation.
   *
   * @return {@code boolean} - Result of validation.
   */
  @Override
  public boolean isValid(UserData value, ConstraintValidatorContext context) {
  
    String role = value.getRole();
    
    if (role.equals("UNKNOWN")) {
      return false;
    }
  
    String batchNumber = value.getBatch();
    String university = value.getUniversity();
    
    if (role.equals("STUDENT")) {
      return batchNumber != null && university != null;
    } else {
      return batchNumber == null && university == null;
    }
  }
  
}
