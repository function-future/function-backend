package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Must implement {@link ConstraintValidator} interface.
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation - The annotation that uses this validator.
   */
  @Override
  public void initialize(Phone constraintAnnotation) {
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
  public boolean isValid(String value, ConstraintValidatorContext context) {
    
    return (value == null) || (value.matches("^(\\+62|0)[0-9]{10,12}$"));
  }
  
}