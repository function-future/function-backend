package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

  @Override
  public void initialize(Phone constraintAnnotation) {
    // No initialization needed for this validator.
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return (value == null) || (value.matches("^(\\+62|0)[0-9]{9,12}$"));
  }

}
