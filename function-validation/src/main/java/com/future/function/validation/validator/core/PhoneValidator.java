package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Phone;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator of the
 * {@link com.future.function.validation.annotation.core.Phone} annotation.
 * Implements  {@link javax.validation.ConstraintValidator} interface with
 * the second type being String as the validated data is String object.
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

  /**
   * Do initialization related to the annotation here.
   *
   * @param constraintAnnotation The annotation that uses this validator.
   */
  @Override
  public void initialize(Phone constraintAnnotation) {
    // No initialization needed for this validator.
  }

  /**
   * Checks whether the String object is not null and matches the specified
   * regex pattern for only numerical characters or with one + sign.
   *
   * @param value   Value of data to be validated.
   * @param context Context of annotation.
   * @return {@code boolean} - Result of validation.
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return (value == null) || (value.matches("^(\\+62|0)[0-9]{10,12}$"));
  }

}
