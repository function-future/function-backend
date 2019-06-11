package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator of the
 * {@link com.future.function.validation.annotation.core.Name} annotation.
 * Implements  {@link javax.validation.ConstraintValidator} interface with
 * the second type being String as the validated data is String object.
 */
public class NameValidator implements ConstraintValidator<Name, String> {

  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation The annotation that uses this validator.
   */
  @Override
  public void initialize(Name constraintAnnotation) {
    // No initialization needed for this validator.
  }

  /**
   * Checks whether the String object is not null and matches the specified
   * regex pattern for alphabetical characters only.
   *
   * @param value   Value of data to be validated.
   * @param context Context of annotation.
   *
   * @return {@code boolean} - Result of validation.
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return (value != null) && (value.matches("^([A-Za-z]+(( )[A-Za-z]+)*)+$"));
  }

}
