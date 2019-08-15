package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Alphanumeric;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlphanumericValidator
  implements ConstraintValidator<Alphanumeric, String> {

  @Override
  public void initialize(Alphanumeric constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return (value != null) && (value.matches("^[A-Za-z0-9]+$"));
  }

}
