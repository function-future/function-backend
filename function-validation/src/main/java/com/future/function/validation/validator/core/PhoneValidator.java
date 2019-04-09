package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Must implement {@link ConstraintValidator} interface.
 */
public class PhoneValidator implements ConstraintValidator<Name, String> {
  
  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation - The annotation that uses this validator.
   */
  @Override
  public void initialize(Name constraintAnnotation) {
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